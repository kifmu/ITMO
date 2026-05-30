package server;

import common.protocol.*;
import util.ServerLogger;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12346;
    private static final ServerLogger logger = ServerLogger.getInstance();

    private static final ExecutorService readPool = Executors.newCachedThreadPool();
    private static final ExecutorService sendPool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        logger.info("Сервер запускается");

        try {
            DatabaseManager dbManager = new DatabaseManager();
            logger.info("Подключение к базе данных");

            if (!dbManager.testConnection()) {
                logger.severe("Не удалось подключиться к базе данных");
                logger.severe("Проверьте, что PostgreSQL запущен и доступен");
                return;
            }
            logger.info("Подключение к базе данных успешно");

            UserDAO userDAO = new UserDAO(dbManager);
            PersonDAO personDAO = new PersonDAO(dbManager);

            CollectionManager collectionManager = new CollectionManager(personDAO);

            collectionManager.loadFromDatabase();
            logger.info("Коллекция загружена, " + collectionManager.size() + " элементов");

            CommandExecutor executor = new CommandExecutor(
                    collectionManager,
                    userDAO,
                    personDAO
            );

            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));
            logger.info("Сервер запущен на порту " + PORT);

            RequestReceiver receiver = new RequestReceiver(channel);
            ResponseSender sender = new ResponseSender(channel);

            while (!Thread.currentThread().isInterrupted()) {
                readPool.submit(() -> {
                    try {
                        var requestData = receiver.receiveNonBlocking();

                        if (requestData == null) {
                            Thread.sleep(50);
                            return;
                        }

                        CommandRequest request = requestData.request;
                        InetSocketAddress clientAddr = requestData.clientAddress;

                        logger.info("Получен: " + request.getType());
                        Thread processingThread = new Thread(() -> {
                            try {
                                CommandResponse response = executor.execute(request);

                                logger.info("Обработан: " + request.getType());
                                sendPool.submit(() -> {
                                    try {
                                        sender.send(response, clientAddr);
                                        logger.info("Отправлен ответ");
                                    } catch (Exception e) {
                                        logger.warning(e.getMessage());
                                    }
                                });

                            } catch (Exception e) {
                                logger.warning(e.getMessage());
                            }
                        });

                        processingThread.start();

                    } catch (Exception e) {
                        logger.warning(e.getMessage());
                    }
                });

                Thread.sleep(10);
            }

        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            readPool.shutdown();
            sendPool.shutdown();

            try {
                readPool.awaitTermination(5, TimeUnit.SECONDS);
                sendPool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.warning("Таймаут завершения пулов");
            }

            logger.info("Сервер остановлен");
        }
    }
}