import commands.*;
import modules.*;
import utils.JsonUtil;
import utils.Runner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CollectionHandler collectionHandler = new CollectionHandler();
        ConsoleManager consoleManager = new ConsoleManager();
        CommandManager commandManager = new CommandManager();

        String filename = System.getenv("COLLECTION_FILE");

        File file = new File(filename);
        if (file.exists() && file.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }

                var loadedCollection = JsonUtil.deserializeCollection(jsonContent.toString());
                collectionHandler.setCollection(loadedCollection);
                collectionHandler.validateIDs();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // Регистрация команд (без изменений)
        commandManager.register("help", new Help());
        commandManager.register("info", new InfoCommand(collectionHandler));
        commandManager.register("exit", new Exit());
        commandManager.register("show", new Show(collectionHandler));
        commandManager.register("clear", new Clear(collectionHandler));
        commandManager.register("insert", new Insert(collectionHandler, consoleManager));
        commandManager.register("history", new History(commandManager));
        commandManager.register("update", new Update(collectionHandler, consoleManager));
        commandManager.register("remove_key", new RemoveKey(collectionHandler));
        commandManager.register("save", new saveCommand(collectionHandler));
        commandManager.register("remove_all_by_nationality", new RemoveAllByNationallity(collectionHandler));
        commandManager.register("print_field_descending_height", new PrintFieldDescendingHeight(collectionHandler));
        commandManager.register("execute_script", new ExecuteScript(collectionHandler, commandManager));
        commandManager.register("print_unique_eye_color", new PrintUniqueEyeColor(collectionHandler));
        commandManager.register("remove_greater", new RemoveGreater(collectionHandler, consoleManager));
        commandManager.register("replace_if_greater", new ReplaceIfGreater(collectionHandler, consoleManager));

        new Runner(commandManager, scanner).runInteractiveMode();

        scanner.close();
        consoleManager.close();
    }
}