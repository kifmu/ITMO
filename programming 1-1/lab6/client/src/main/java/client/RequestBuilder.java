package client;

import common.protocol.CommandRequest;
import common.protocol.CommandType;
import common.models.Person;
import client.util.InputValidator;
import java.util.Scanner;

public class RequestBuilder {
    private final Scanner scanner;

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
            Person personData = null;
            if (type == CommandType.INSERT || type == CommandType.UPDATE ||
                    type == CommandType.REMOVE_GREATER || type == CommandType.REPLACE_IF_GREATER) {

                System.out.println("Введите данные объекта:");
                personData = InputValidator.readPerson();
                if (personData == null) return null;
            }
            return new CommandRequest(type, arg, personData);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестная команда: " + cmdName);
            return null;
        }
    }
}