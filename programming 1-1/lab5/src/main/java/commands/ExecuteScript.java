package commands;

import modules.CollectionHandler;
import modules.CommandManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
/**
 * Команда 'execute_script'. Выполнить скрипт из файла.
 * @author kifmu
 */
public class ExecuteScript implements Command {
    private final CollectionHandler collectionHandler;
    private final CommandManager commandManager;

    private static final Set<String> executingScripts = new HashSet<>();


    public ExecuteScript(CollectionHandler collectionHandler, CommandManager commandManager) {

        this.collectionHandler = collectionHandler;
        this.commandManager = commandManager;
    }
    /**
     * Выполняет команду
     * @return Результат выполнения команды.
     */
    @Override
    public String execute(String[] arguments) {

        if (arguments == null || arguments.length == 0 || arguments[0].trim().isEmpty()) {
            return "Не указан файл. Используйте execute_script <имя_файла>";
        }

        String fileName = arguments[0].trim();
        File file = new File(fileName);

        if (!file.exists()) {
            return "Файл '" + fileName + "' не найден.";
        }

        if (!file.isFile()) {
            return "'" + fileName + "' не является файлом.";
        }

        if (!file.canRead()) {
            return "Нет прав на чтение файла '" + fileName + "'. " +
                    "Проверьте разрешения доступа.";
        }

        String absolutePath;
        try {
            absolutePath = file.getAbsolutePath();
        } catch (SecurityException e) {
            return "Доступ к пути файла запрещён системой безопасности.";
        }

        if (executingScripts.contains(absolutePath)) {
            return "Файл '" + fileName + "' уже выполняется.";
        }

        try {
            executingScripts.add(absolutePath);

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    String[] parts = line.split("\\s+", 2);
                    String cmdName = parts[0];
                    String[] cmdArgs;

                    if (parts.length > 1) {
                        String argsPart = parts[1];

                        if ("insert".equals(cmdName)) {
                            int jsonStart = argsPart.indexOf('{');
                            if (jsonStart != -1) {
                                String key = argsPart.substring(0, jsonStart).trim();
                                String json = argsPart.substring(jsonStart).trim();
                                cmdArgs = new String[]{key, json};
                            } else {
                                cmdArgs = new String[]{argsPart.trim()};
                            }
                        } else {
                            cmdArgs = new String[]{argsPart.trim()};
                        }
                    } else {
                        cmdArgs = new String[0];
                    }

                    try {
                        if ("execute_script".equals(cmdName)) {
                            System.err.println("Вложенные скрипты запрещены");
                            continue;
                        }

                        commandManager.execute(cmdName, cmdArgs);

                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }

                return "Скрипт '" + fileName + "' выполнен.";
            }

        } catch (IOException e) {
            String errorMsg = e.getMessage();

            if (errorMsg != null) {
                if (errorMsg.contains("Permission denied") || errorMsg.contains("Отказано в доступе")) {
                    return "Нет прав доступа к файлу '" + fileName + "'. " +
                            "Попробуйте запустить программу от имени администратора или измените права на файл.";
                } else if (errorMsg.contains("Is a directory")) {
                    return fileName + "' является директорией, а не файлом.";
                }
            }

            return "Ошибка чтения файла '" + fileName + "': " + errorMsg;

        } catch (SecurityException e) {
            return "Программа не имеет прав на доступ к файлу '" + fileName + "'. " +
                    "Измените права или проверьте настройки.";

        } finally {
            executingScripts.remove(absolutePath);
        }
    }
    /**
     * Получить описание команды.
     * @return Описание команды.
     */
    @Override
    public String getDescription() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла";
    }
    /**
     * Получить название команды.
     * @return Название команды.
     */
    @Override
    public String getName() {
        return "execute_script";
    }
}