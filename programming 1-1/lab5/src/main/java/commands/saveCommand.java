package commands;

import modules.CollectionHandler;
import utils.JsonUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Команда 'save'. Сохраняет текущую коллекцию в файл.
 * @author kifmu
 */
public class saveCommand implements Command {
    CollectionHandler collectionHandler;

    public saveCommand(CollectionHandler collectionHandler) {
        this.collectionHandler = collectionHandler;
    }
    /**
     * Выполняет команду сохранения коллекции в файл.
     * @param arguments аргументы команды
     * @return сообщение о результате выполнения
     */
    @Override
    public String execute(String[] arguments) {
        String filename = System.getenv("COLLECTION_FILE");

        if (filename == null || filename.isEmpty()) {
            return "Переменная окружения не установлена.";
        }

        try {
            String json = JsonUtil.serializeCollection(collectionHandler.getCollection());

            try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
                byte[] jsonBytes = json.getBytes();
                fileOutputStream.write(jsonBytes);
            }
            return "Коллекция успешно сохранена в файл " + filename;
        } catch (FileNotFoundException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "save : сохранить коллекцию в файл";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "save";
    }

}