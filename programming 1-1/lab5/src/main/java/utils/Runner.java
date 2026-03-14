package utils;

import modules.CommandManager;
import java.util.Scanner;

/**
 * Класс для запуска интерактивного режима работы приложения.
 * @author kifmu
 */
public class Runner {
    private final CommandManager commandManager;
    private final Scanner scanner;

    public Runner(CommandManager commandManager, Scanner scanner) {
        this.commandManager = commandManager;
        this.scanner = scanner;
    }

    /**
     * Запускает интерактивный режим работы программы.
     * Считывает команды из консоли и передает их на выполнение.
     */
    public void runInteractiveMode() {
        System.out.println("Введите 'help' для получения справки.");
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            commandManager.addCommandHistory(line);

            String[] parts = line.split("\\s+", 2);
            String commandName = parts[0];
            String[] args;

            if (parts.length > 1) {
                String argsPart = parts[1];
                if ("insert".equals(commandName) || "remove_greater".equals(commandName) || "replace_if_greater".equals(commandName) || "update".equals(commandName)) {
                    int jsonStart = argsPart.indexOf('{');
                    if (jsonStart != -1) {
                        String key = argsPart.substring(0, jsonStart).trim();
                        String json = argsPart.substring(jsonStart).trim();
                        args = new String[]{key, json};
                    } else {
                        args = new String[]{argsPart.trim()};
                    }
                } else {
                    args = argsPart.split("\\s+");
                }
            } else {
                args = new String[0];
            }

            if ("exit".equals(commandName)) {
                System.out.println("Работа завершена.");
                break;
            }

            try {
                commandManager.execute(commandName, args);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}