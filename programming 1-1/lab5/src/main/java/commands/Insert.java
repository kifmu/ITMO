package commands;

import exceptions.ValidationException;
import models.*;
import modules.CollectionHandler;
import modules.ConsoleManager;
import utils.JsonUtil;

/**
 * Команда 'insert'. Добавляет новый элемент в коллекцию с заданным ключом.
 * @author kifmu
 */
public class Insert implements Command {
    private final CollectionHandler collectionHandler;
    private static ConsoleManager consoleManager = new ConsoleManager();

    public Insert(CollectionHandler collectionHandler, ConsoleManager consoleManager) {
        this.collectionHandler = collectionHandler;
        Insert.consoleManager = consoleManager;
    }

    /**
     * Выполняет команду добавления нового элемента в коллекцию.
     * @param arguments аргументы команды: [0] - ключ или "ключ {JSON}", [1] - JSON-объект
     * @return сообщение о результате выполнения команды
     */
    @Override
    public String execute(String[] arguments) {
        try {
            String key = null;
            Person person = null;

            if (arguments != null && arguments.length > 0) {
                String firstArg = arguments[0].trim();

                if (!"null".equals(firstArg)) {
                    key = firstArg;
                }

                if (arguments.length > 1) {
                    String jsonElement = arguments[1].trim();
                    try {
                        person = JsonUtil.deserializePerson(jsonElement);
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                } else {
                    String fullArg = arguments[0];
                    int firstSpace = fullArg.indexOf(' ');
                    if (firstSpace != -1) {
                        String potentialJson = fullArg.substring(firstSpace + 1).trim();
                        if (potentialJson.startsWith("{")) {
                            try {
                                person = JsonUtil.deserializePerson(potentialJson);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }

            if (key != null && collectionHandler.checkExist(key)) {
                return "Элемент с таким ключом уже существует. Введите другой ключ.";
            }

            if (person == null) {
                person = readPerson();
            }

            boolean added = collectionHandler.addToCollection(key, person);
            return added ? "Элемент успешно добавлен." : "Ошибка при добавлении.";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Считывает данные нового объекта Person из консоли в интерактивном режиме.
     * @return новый объект Person с валидными данными
     * @throws RuntimeException если произошла непредвиденная ошибка ввода
     */
    public static Person readPerson() {
        try {
            String name;
            while (true) {
                consoleManager.write("Введите имя: ");
                name = consoleManager.readLine().trim();
                if (!name.isEmpty()) {
                    break;
                }
                consoleManager.write("Имя не может быть пустым. Попробуйте снова.");
            }

            float x;
            while (true) {
                consoleManager.write("Введите координату X: ");
                String strX = consoleManager.readLine().trim();
                try {
                    x = Float.parseFloat(strX);
                    if (x <= -690) {
                        throw new ValidationException("Значение поля должно быть больше -690");
                    }
                    break;
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат. Введите число.");
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
                    if (y > 28) {
                        throw new ValidationException("Максимальное значение поля: 28.");
                    }
                    break;
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат. Введите число.");
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
                    if (height <= 0) {
                        throw new ValidationException("Значение поля должно быть больше 0.");
                    }
                    break;
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат. Введите целое число.");
                } catch (ValidationException e) {
                    consoleManager.write(e.getMessage());
                }
            }

            String passportID = null;
            while (passportID == null) {
                consoleManager.write("Введите номер паспорта (минимум 9 символов) или Enter для пропуска: ");
                String input = consoleManager.readLine().trim();
                if (input.isEmpty()) {
                    break;
                }
                if (input.length() < 9) {
                    consoleManager.write("Длина номера паспорта должна быть не менее 9 символов.\n");
                    continue;
                }
                passportID = input;
            }

            Color color = null;
            while (true) {
                consoleManager.write("Введите цвет глаза [BLACK, BLUE, BROWN] или Enter для пропуска: ");
                String clrStr = consoleManager.readLine().trim();
                if (clrStr.isEmpty()) {
                    break;
                }

                try {
                    color = Color.valueOf(clrStr.toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    consoleManager.write("Неверный цвет. Доступные варианты: [BLACK, BLUE, BROWN]");
                }
            }

            Country nationality = null;
            while (nationality == null) {
                consoleManager.write("Введите национальность [ITALY, THAILAND, NORTH_KOREA]: ");
                String input = consoleManager.readLine().trim().toUpperCase();
                try {
                    nationality = Country.valueOf(input);
                } catch (IllegalArgumentException e) {
                    consoleManager.write("Неверная национальность. Доступные варианты: ITALY, THAILAND, NORTH_KOREA");
                }
            }

            Float locX = null;
            while (locX == null) {
                consoleManager.write("Введите координату X места: ");
                String input = consoleManager.readLine().trim();
                try {
                    locX = Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат. Введите число.");
                }
            }

            long locY;
            while (true) {
                consoleManager.write("Введите координату Y места: ");
                String input = consoleManager.readLine().trim();
                try {
                    locY = Long.parseLong(input);
                    break;
                } catch (NumberFormatException e) {
                    consoleManager.write("Неверный формат. Введите целое число.");
                }
            }

            String locName = null;
            while (locName == null) {
                consoleManager.write("Введите название места: ");
                String input = consoleManager.readLine().trim();
                try {
                    if (input.isEmpty()) {
                        throw new ValidationException("Название не может быть пустым");
                    }
                    if (input.length() > 811) {
                        throw new ValidationException("Длина названия не должна превышать 811 символов");
                    }
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
        return "insert key {element} : добавить новый элемент с заданным ключом (ключ или JSON элемента)";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "insert";
    }
}