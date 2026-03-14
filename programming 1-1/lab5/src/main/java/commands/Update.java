package commands;

import exceptions.ValidationException;
import models.*;
import modules.CollectionHandler;
import modules.ConsoleManager;
import utils.JsonUtil;

import java.util.LinkedHashMap;

/**
 * Команда 'update'. Обновляет элемент коллекции по его ID.
 * Позволяет заменить существующий объект Person на новый, сохраняя при этом оригинальный ID и дату создания.
 * @author kifmu
 */
public class Update implements Command {
    private final CollectionHandler collectionHandler;
    private final ConsoleManager consoleManager;

    public Update(CollectionHandler collectionHandler, ConsoleManager consoleManager) {
        this.collectionHandler = collectionHandler;
        this.consoleManager = consoleManager;
    }
    /**
     * Выполняет команду обновления элемента коллекции.
     * @param arguments аргументы команды: [0] - ID элемента, [1] - JSON-представление нового элемента
     * @return сообщение о результате выполнения команды
     */
    @Override
    public String execute(String[] arguments) {
        try {
            if (arguments == null || arguments.length == 0) {
                return "Не указан ID элемента для обновления. Используйте update <id> {element}";
            }

            int neededID;
            try {
                neededID = Integer.parseInt(arguments[0].trim());
            } catch (NumberFormatException e) {
                return "Неверный формат для ID. Введите целое число.";
            }

            String foundKey = null;
            Person oldPerson = null;
            LinkedHashMap<String, Person> collection = collectionHandler.getCollection();

            for (String key : collection.keySet()) {
                if (collection.get(key).getId() == neededID) {
                    foundKey = key;
                    oldPerson = collection.get(key);
                    break;
                }
            }

            if (foundKey == null || oldPerson == null) {
                return "Элемент с ID " + neededID + " не найден в коллекции.";
            }

            Person newPerson = null;

            if (arguments != null && arguments.length > 1) {
                String jsonElement = arguments[1].trim();
                try {
                    newPerson = JsonUtil.deserializePerson(jsonElement);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }

            if (newPerson == null) {
                newPerson = readPerson();
            }

            newPerson.setId(oldPerson.getId());
            newPerson.setCreationDate(oldPerson.getCreationDate());

            collection.put(foundKey, newPerson);

            return "Элемент с ID " + neededID + " успешно обновлён.";

        } catch (Exception e) {
            return e.getMessage();
        }
    }
    /**
     * Считывает данные нового объекта Person из консоли в интерактивном режиме.
     * @return новый объект Person с валидными данными
     * @throws RuntimeException если произошла непредвиденная ошибка ввода
     */
    private Person readPerson() {
        try {
            String name;
            while (true) {
                consoleManager.write("Введите имя: ");
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

            int height;
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
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "update";
    }
}