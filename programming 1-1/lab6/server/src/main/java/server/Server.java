package server;

import util.ServerLogger;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Scanner;

public class Server {
    private static final int port = 12345;
    private static final ServerLogger logger = ServerLogger.getInstance();

    public static void main(String[] args) {
        logger.info("Сервер запущен");

        CollectionManager collectionManager = null;
        DatagramChannel channel = null;
        Selector selector = null;

        try {
            collectionManager = new CollectionManager();
            collectionManager.loadFromFile();
            logger.info("Коллекция загружена. Элементов: " + collectionManager.size());

            final CollectionManager finalCollectionManager = collectionManager;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Завершение работы");
                finalCollectionManager.saveToFile();
                logger.info("Коллекция сохранена");
            }));


            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(port));
            logger.info("UDP-канал открыт на порту " + port);

            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            RequestReceiver receiver = new RequestReceiver(channel);
            CommandExecutor executor = new CommandExecutor(collectionManager);
            ResponseSender sender = new ResponseSender(channel);

            CollectionManager finalCollectionManager1 = collectionManager;
            new Thread(() -> {
                Scanner console = new Scanner(System.in);
                logger.info("Введите 'save' для сохранения коллекции");

                while (true) {
                    try {
                        String cmd = console.nextLine().trim().toLowerCase();

                        if ("save".equals(cmd)) {
                            finalCollectionManager1.saveToFile();
                            logger.info("Коллекция сохранена вручную");
                        } else if ("exit".equals(cmd)) {
                            logger.info("Завершение работы по команде");
                            finalCollectionManager1.saveToFile();
                            break;
                        }
                    } catch (Exception ignored) {

                    }
                }
            }).start();

            logger.info("Сервер готов к работе");

            while (true) {
                selector.select();

                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isReadable()) {
                        handleRequest(key, receiver, executor, sender);
                    }
                    selector.selectedKeys().remove(key);
                }
            }

        } catch (Exception e) {
            logger.severe( e.getMessage());
            e.printStackTrace();
        } finally {
            if (collectionManager != null) {
                logger.info("Завершение работы сервера");
                collectionManager.saveToFile();
                logger.info("Коллекция сохранена");
            }

            try {
                if (selector != null) {
                    selector.close();
                }
                if (channel != null) {
                    channel.close();
                }
            } catch (Exception ignored) {
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static void handleRequest(SelectionKey key,
                                      RequestReceiver receiver,
                                      CommandExecutor executor,
                                      ResponseSender sender) {
        try {
            common.protocol.CommandRequest request = receiver.receive(key);
            if (request == null) return;

            logger.info("Запрос по команде " + request.getType());

            common.protocol.CommandResponse response = executor.execute(request);

            sender.send(response, key);
            logger.info("Ответ отправлен");

        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }
}