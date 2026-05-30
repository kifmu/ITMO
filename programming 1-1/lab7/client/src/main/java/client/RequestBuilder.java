package client;

import client.util.InputValidator;
import common.models.Person;
import common.protocol.CommandRequest;
import common.protocol.CommandType;

import java.util.Scanner;

public class RequestBuilder {
    private final Scanner scanner;
    private String currentUsername = null;
    private String currentPassword = null;

    public RequestBuilder() {
        this.scanner = new Scanner(System.in);
    }

    public CommandRequest build(String input) {
        Client.addToHistory(input);
        String[] parts = input.trim().split("\\s+", 2);
        String cmdName = parts[0].toUpperCase();
        String arg = parts.length > 1 ? parts[1] : null;

        try {
            CommandType type = CommandType.valueOf(cmdName);

            if (type.isClientOnly()) {
                return new CommandRequest(type, null, null);
            }

            if (type.isServerOnly()) {
                System.out.println("Команда '" + type + "' доступна только серверу");
                return null;
            }

            if (type == CommandType.REGISTER || type == CommandType.LOGIN) {
                System.out.println("Введите логин:");
                String username = scanner.nextLine().trim();
                System.out.println("Введите пароль:");
                String password = scanner.nextLine().trim();

                if (type == CommandType.LOGIN) {
                    currentUsername = username;
                    currentPassword = password;
                }

                return new CommandRequest(type, arg, null, username, password);
            }
            if (currentUsername == null || currentPassword == null) {
                System.out.println("Требуется авторизация. Выполните команду 'login'");
                return null;
            }
            Person personData = null;
            if (type == CommandType.INSERT || type == CommandType.UPDATE ||
                    type == CommandType.REMOVE_GREATER || type == CommandType.REPLACE_IF_GREATER) {

                System.out.println("Введите данные объекта:");
                personData = InputValidator.readPerson();
                if (personData == null) return null;
            }
            return new CommandRequest(type, arg, personData, currentUsername, currentPassword);

        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестная команда: " + cmdName);
            return null;
        }
    }
}