package commands;

import exceptions.NonexistedEnumValue;
import models.Country;
import models.Person;
import modules.CollectionHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Команда 'remove_all_by_nationality'. Удаляет из коллекции все элементы с заданной национальностью.
 * @author kifmu
 */
public class RemoveAllByNationallity implements Command {
    CollectionHandler collectionHandler;

    public RemoveAllByNationallity(CollectionHandler collectionHandler) {
        this.collectionHandler = collectionHandler;
    }
    /**
     * Выполняет команду удаления всех элементов с указанной национальностью.
     * @param arguments аргументы команды: [0] - название национальности
     * @return сообщение о результате выполнения с количеством удалённых элементов
     * @throws NonexistedEnumValue если указана несуществующая национальность
     */
    @Override
    public String execute(String[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return "Не указана национальность. Введите корректную национальность.";
        }

        Country nationality;
        try {
            nationality = Country.valueOf(arguments[0].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NonexistedEnumValue("Национальность '" + arguments[0] + "' не существует.");
        }

        LinkedHashMap<String, Person> collection = collectionHandler.getCollection();

        List<String> keysToRemove = new ArrayList<>();

        for (String key : collection.keySet()) {
            if (collection.get(key).getNationality() == nationality) {
                keysToRemove.add(key);
            }
        }

        for (String key : keysToRemove) {
            collection.remove(key);
        }

        if (!keysToRemove.isEmpty()) {
            return "Удалено элементов с национальностью " + nationality + ": " + keysToRemove.size();
        } else {
            return "Элементы с национальностью " + nationality + " не найдены.";
        }
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "remove_all_by_nationality nationality : удалить из коллекции все элементы, значение поля nationality которого эквивалентно заданному";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "remove_all_by_nationality";
    }
}