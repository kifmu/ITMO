package commands;

import models.Color;
import models.Person;
import modules.CollectionHandler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Команда 'print_unique_eye_color'. Выводит уникальные значения цвета глаз
 * @author kifmu
 */
public class PrintUniqueEyeColor implements Command {
    CollectionHandler collectionHandler;

    public PrintUniqueEyeColor(CollectionHandler collectionHandler) {
        this.collectionHandler = collectionHandler;
    }
    /**
     * Выполняет команду вывода уникальных значений цвета глаз
     * @param arguments аргументы команды
     * @return строка с перечислением уникальных значений цвета глаз
     * @throws FileNotFoundException не используется, оставлен для совместимости с интерфейсом
     */
    @Override
    public String execute(String[] arguments) throws FileNotFoundException {
        LinkedHashMap<String, Person> collection = collectionHandler.getCollection();
        ArrayList<Color> uniqueColors = new ArrayList<>();

        for (String key : collection.keySet()) {
            if (uniqueColors.contains(collection.get(key).getEyeColor())) {
                continue;
            } else {
                uniqueColors.add(collection.get(key).getEyeColor());
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("Уникальные значения поля eyeColor всех элементов в коллекции: ");
        for (Color color : uniqueColors) {
            report.append("\n").append(color);
        }
        return report.toString();
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "print_unique_eye_color : вывести уникальные значения поля eyeColor всех элементов в коллекции";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "print_unique_eye_color";
    }
}