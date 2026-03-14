package commands;

import models.Person;
import modules.CollectionHandler;

import java.util.LinkedHashMap;

/**
 * Команда 'show'. Выводит все элементы коллекции в строковом представлении.
 * @author kifmu
 */
public class Show implements Command {
    private final CollectionHandler collectionHandler;

    public Show(CollectionHandler collectionHandler) {
        this.collectionHandler = collectionHandler;
    }

    /**
     * Выполняет команду вывода всех элементов коллекции.
     * @param arguments аргументы команды
     * @return строка со всеми элементами коллекции
     */
    @Override
    public String execute(String[] arguments) {
        LinkedHashMap<String, Person> collection = collectionHandler.getCollection();

        if (collection == null || collection.isEmpty()) {
            return "Коллекция пуста.";
        }

        StringBuilder result = new StringBuilder();
        for (String keys : collection.keySet()) {
            result.append(keys).append(": ").append(collection.get(keys)).append("\n");
        }

        return result.toString();
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "show : вывести все элементы коллекции в строковом представлении";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "show";
    }
}