package commands;

import models.Person;
import modules.CollectionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * Команда 'print_field_descending_height'. Выводит значения поля height
 * всех элементов коллекции в порядке убывания.
 * @author kifmu
 */
public class PrintFieldDescendingHeight implements Command {
    CollectionHandler collectionHandler;

    public PrintFieldDescendingHeight(CollectionHandler collectionHandler) {
        this.collectionHandler = collectionHandler;
    }
    /**
     * Выполняет команду вывода значений поля height по убыванию.
     * @param arguments аргументы команды
     * @return строка с перечислением значений роста в порядке убывания
     */
    @Override
    public String execute(String[] arguments) {
        LinkedHashMap<String, Person> collection = collectionHandler.getCollection();
        if (collection.isEmpty()) {
            return "Коллекция пуста.";
        }
        ArrayList<Integer> heights = new ArrayList<>();

        for (String key : collection.keySet()) {
            heights.add(collection.get(key).getHeight());
        }

        Collections.sort(heights, Collections.reverseOrder());

        StringBuilder result = new StringBuilder("Значения поля height (по убыванию):\n");
        for (int i = 0; i < heights.size(); i++) {
            result.append(heights.get(i));
            if (i < heights.size() - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "print_field_descending_height : вывести значения поля height всех элементов в порядке убывания";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "print_field_descending_height";
    }
}