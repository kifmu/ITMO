package commands;

import models.Person;
import modules.CollectionHandler;

import java.util.LinkedHashMap;

/**
 * Команда 'remove_key'. Удаляет элемент из коллекции по его ключу.
 * @author kifmu
 */
public class RemoveKey implements Command {
    private final CollectionHandler collectionHandler;

    public RemoveKey(CollectionHandler collectionHandler) {
        this.collectionHandler = collectionHandler;
    }
    /**
     * Выполняет команду удаления элемента по ключу.
     * @param arguments аргументы команды: [0] - ключ элемента для удаления
     * @return сообщение о результате выполнения команды
     */
    @Override
    public String execute(String[] arguments) {
        try {
            if (arguments == null || arguments.length == 0) {
                return "Не указан ключ элемента для удаления. Используйте команду формата remove_key <key>";
            }

            LinkedHashMap<String, Person> collection = collectionHandler.getCollection();

            if (collection == null || collection.isEmpty()) {
                return "Коллекция пуста, невозможно удалить элемент.";
            }

            String neededKey = arguments[0].trim();
            if (collection.containsKey(neededKey)) {
                collection.remove(neededKey);
                return "Элемент с ключом " + neededKey + " успешно удален.";
            } else {
                return "Элемент с ключом " + neededKey + " не найден в коллекции.";
            }

        } catch (Exception e) {
            return e.getMessage();
        }
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "remove_key <key> : удалить элемент из коллекции по его ключу";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "remove_key";
    }
}