package commands;

import exceptions.ValidationException;
import models.*;
import modules.CollectionHandler;
import modules.ConsoleManager;
import utils.JsonUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Команда 'remove_greater'. Удаляет из коллекции все элементы, превышающие заданный объект.
 * @author kifmu
 */
public class RemoveGreater implements Command {
    private final CollectionHandler collectionHandler;
    private ConsoleManager consoleManager = null;

    public RemoveGreater(CollectionHandler collectionHandler, ConsoleManager consoleManager) {
        this.collectionHandler = collectionHandler;
        this.consoleManager = consoleManager;
    }
    /**
     * Выполняет команду удаления элементов, превышающих заданный объект.
     * @param arguments аргументы команды: [0] - (не используется), [1] - JSON-представление объекта для сравнения
     * @return сообщение о результате выполнения команды с количеством удалённых элементов
     */
    @Override
    public String execute(String[] arguments) {
        try {
            Person comparisonPerson = null;
            if (arguments != null && arguments.length > 1) {
                String jsonElement = arguments[1].trim();
                try {
                    comparisonPerson = JsonUtil.deserializePerson(jsonElement);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
            if (comparisonPerson == null) {
                comparisonPerson = readPerson();
            }

            LinkedHashMap<String, Person> collection = collectionHandler.getCollection();
            List<String> keysToRemove = new ArrayList<>();

            for (String key : collection.keySet()) {
                Person person = collection.get(key);
                if (person.compareTo(comparisonPerson) > 0) {
                    keysToRemove.add(key);
                }
            }
            if (keysToRemove.isEmpty()) {
                return "Нет элементов, превышающих заданный.";
            }
            for (String key : keysToRemove) {
                collection.remove(key);
            }
            return "Удалено " + keysToRemove.size() + " элементов.";

        } catch (Exception e) {
            return e.getMessage();
        }
    }
    /**
     * Считывает данные объекта Person из консоли в интерактивном режиме для сравнения.
     * @return объект Person с валидными данными для сравнения
     * @throws RuntimeException если произошла непредвиденная ошибка ввода
     */
    private Person readPerson() {
        try {
            String name;
            while (true) {
                consoleManager.write("Введите имя для сравнения: ");
                name = consoleManager.readLine().trim();
                if (!name.isEmpty()) break;
                consoleManager.write("Имя не может быть пустым.");
            }

            float x;
            while (true) {
                consoleManager.write("Введите координату X: ");
                String strX = consoleManager.readLine().trim();
                try {
                    x = Float.parseFloat(strX);
                    if (x <= -690) throw new ValidationException("X должно быть больше -690");
                    break;
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат.");
                } catch (ValidationException e) {
                    consoleManager.write(e.getMessage());
                }
            }

            double y;
            while (true) {
                consoleManager.write("Введите координату Y: ");
                String strY = consoleManager.readLine().trim();
                try {
                    y = Double.parseDouble(strY);
                    if (y > 28) throw new ValidationException("Y не может быть больше 28");
                    break;
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат.");
                } catch (ValidationException e) {
                    consoleManager.write(e.getMessage());
                }
            }
            Coordinates coordinates = new Coordinates(x, y);

            int height = 0;
            while (true) {
                consoleManager.write("Введите рост: ");
                String strHeight = consoleManager.readLine().trim();
                try {
                    height = Integer.parseInt(strHeight);
                    if (height <= 0) throw new ValidationException("Рост должен быть > 0");
                    break;
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат.");
                } catch (ValidationException e) {
                    consoleManager.write(e.getMessage());
                }
            }

            String passportID = null;
            while (passportID == null) {
                consoleManager.write("Паспорт (мин. 9 символов) или Enter: ");
                String input = consoleManager.readLine().trim();
                if (input.isEmpty()) break;
                if (input.length() < 9) {
                    consoleManager.write("Длина >= 9 символов.");
                    continue;
                }
                passportID = input;
            }

            Color color = null;
            while (color == null) {
                consoleManager.write("Цвет глаз [BLACK, BLUE, BROWN]: ");
                String clrStr = consoleManager.readLine().trim().toUpperCase();
                try {
                    color = Color.valueOf(clrStr);
                } catch (IllegalArgumentException e) {
                    consoleManager.write("Неверный цвет.");
                }
            }

            Country nationality = null;
            while (nationality == null) {
                consoleManager.write("Национальность [ITALY, THAILAND, NORTH_KOREA]: ");
                String input = consoleManager.readLine().trim().toUpperCase();
                try {
                    nationality = Country.valueOf(input);
                } catch (IllegalArgumentException e) {
                    consoleManager.write("Неверная национальность.");
                }
            }

            Float locX = null;
            while (locX == null) {
                consoleManager.write("Координата X места: ");
                String input = consoleManager.readLine().trim();
                try {
                    locX = Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат.");
                }
            }

            long locY = 0;
            while (true) {
                consoleManager.write("Координата Y места: ");
                String input = consoleManager.readLine().trim();
                try {
                    locY = Long.parseLong(input);
                    break;
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат.");
                }
            }

            String locName = null;
            while (locName == null) {
                consoleManager.write("Название места: ");
                String input = consoleManager.readLine().trim();
                try {
                    if (input.isEmpty()) throw new ValidationException("Не может быть пустым");
                    if (input.length() > 811) throw new ValidationException("Длина <= 811");
                    locName = input;
                } catch (ValidationException e) {
                    consoleManager.write(e.getMessage());
                }
            }

            Location location = new Location(locX, locY, locName);
            return new Person(name, coordinates, height, passportID, color, nationality, location);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "remove_greater";
    }
}