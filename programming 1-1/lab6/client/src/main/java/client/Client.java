package client;

import common.protocol.*;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Клиентское приложение для интерактивной работы с сервером.
 */
public class Client {
    private static final String server_host = "localhost";
    private static final int server_port = 12345;
    private static final long response_timeout = 3000;
    private static final int history_limit = 9;

    private static final List<String> commandHistory = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Сервер: " + server_host + ":" + server_port);
        System.out.println("Введите 'help' для справки\n");

        try (DatagramChannel channel = DatagramChannel.open()) {

            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(server_host, server_port));

            ConsoleReader console = new ConsoleReader(new Scanner(System.in));
            RequestBuilder builder = new RequestBuilder();
            UdpSender sender = new UdpSender(channel);
            ResponseHandler handler = new ResponseHandler(channel, response_timeout);

            System.out.println("Проверка доступности сервера");
            boolean serverAvailable = checkServerAvailability(sender, handler);

            if (!serverAvailable) {
                System.out.println("Сервер не отвечает");
            } else {
                System.out.println("Сервер доступен\n");
            }

            while (true) {
                System.out.print("> ");
                String input = console.readLine();

                if (input.isEmpty()) {
                    continue;
                }

                if (input.equalsIgnoreCase("history")) {
                    printHistory();
                    continue;
                }
                CommandRequest request = builder.build(input);
                if (request == null) {
                    continue;
                }
                if (request.getType() == CommandType.EXIT) {
                    System.out.println("Завершение работы клиента");
                    break;
                }

                try {
                    sender.send(request);

                    CommandResponse response = handler.receive();

                    if (response != null) {
                        System.out.println(response.getMessage());
                        if (response.getData() != null && !response.getData().isEmpty()) {
                            response.getData().forEach(System.out::println);
                        }
                    } else {
                        System.out.println("Сервер не ответил");
                        System.out.println("Проверьте, что сервер запущен и доступен");
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Проверьте подключение к серверу");
                }

                System.out.println();
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Клиент завершил работу.");
    }

    /**
     * Проверяет доступность сервера тестовым запросом.
     */
    private static boolean checkServerAvailability(UdpSender sender, ResponseHandler handler) {
        try {
            CommandRequest pingRequest = new CommandRequest(CommandType.HELP, null, null);
            sender.send(pingRequest);
            CommandResponse pingResponse = handler.receive();
            return pingResponse != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Выводит историю команд.
     */
    private static void printHistory() {
        if (commandHistory.isEmpty()) {
            System.out.println("История команд пуста");
            return;
        }
        System.out.println("История последних команд:");
        int start = Math.max(0, commandHistory.size() - history_limit);
        for (int i = start; i < commandHistory.size(); i++) {
            System.out.println((i + 1) + ": " + commandHistory.get(i));
        }
    }
    /**
     * Добавляет команду в историю.
     */
    public static void addToHistory(String command) {
        commandHistory.add(command);
        if (commandHistory.size() > history_limit) {
            commandHistory.remove(0);
        }
    }
}