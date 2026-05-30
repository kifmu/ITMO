package server;

import common.models.*;
import common.protocol.*;
import server.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CommandExecutor {
    private final CollectionManager collectionManager;
    private final UserDAO userDAO;
    private final PersonDAO personDAO;

    public CommandExecutor(CollectionManager collectionManager,
                           UserDAO userDAO,
                           PersonDAO personDAO) {
        this.collectionManager = collectionManager;
        this.userDAO = userDAO;
        this.personDAO = personDAO;
    }

    public CommandResponse execute(CommandRequest request) {
        try {
            CommandType type = request.getType();
            if (type == CommandType.REGISTER) {
                return processRegister(request);
            }

            if (type == CommandType.LOGIN) {
                return processLogin(request);
            }
            String username = request.getUsername();
            String password = request.getPassword();
            if (username == null || password == null) {
                return new CommandResponse(false, "Требуется авторизация");
            }

            String passwordHash = PasswordHasher.hash(password);
            int userId = userDAO.authenticate(username, passwordHash);

            if (userId == -1) {
                return new CommandResponse(false, "Неверный логин или пароль");
            }
            return switch (type) {
                case HELP -> new CommandResponse(true, getHelpText());
                case INFO -> processInfo();
                case SHOW -> processShow();
                case PRINT_FIELD_DESCENDING_HEIGHT -> processPrintHeight();
                case PRINT_UNIQUE_EYE_COLOR -> processUniqueEyeColor();
                case INSERT -> processInsert(request, userId);
                case UPDATE -> processUpdate(request, userId);
                case REMOVE_KEY -> processRemoveKey(request, userId);
                case REMOVE_GREATER -> processRemoveGreater(request, userId);
                case REMOVE_ALL_BY_NATIONALITY -> processRemoveByNationality(request, userId);
                case REPLACE_IF_GREATER -> processReplaceIfGreater(request, userId);
                case CLEAR -> processClear(userId);
                case EXECUTE_SCRIPT -> processExecuteScript(request, userId);
                case HISTORY -> new CommandResponse(true, "История на клиенте");
                default -> new CommandResponse(false, "Неизвестная команда");
            };

        } catch (Exception e) {
            return new CommandResponse(false, "Ошибка: " + e.getMessage());
        }
    }
    private CommandResponse processRegister(CommandRequest request) {
        try {
            String username = request.getUsername();
            String password = request.getPassword();

            if (username == null || password == null || username.isEmpty()) {
                return new CommandResponse(false, "Укажите логин и пароль");
            }

            if (password.length() < 4) {
                return new CommandResponse(false, "Пароль должен быть >= 4 символов");
            }

            String passwordHash = PasswordHasher.hash(password);
            boolean success = userDAO.register(username, passwordHash);

            if (success) {
                return new CommandResponse(true, "Регистрация успешна. Войдите с этими данными через login");
            } else {
                return new CommandResponse(false, "Пользователь уже существует");
            }
        } catch (SQLException e) {
            return new CommandResponse(false,e.getMessage());
        }
    }
    private CommandResponse processLogin(CommandRequest request) {
        try {
            String username = request.getUsername();
            String password = request.getPassword();

            String passwordHash = PasswordHasher.hash(password);

            if (userDAO.authenticate(username, passwordHash) != -1) {
                return new CommandResponse(true, "Вход выполнен");
            } else {
                return new CommandResponse(false, "Неверные данные");
            }
        } catch (SQLException e) {
            return new CommandResponse(false, "Ошибка БД");
        }
    }

    private CommandResponse processInfo() {
        return new CommandResponse(true,
                "Тип: ConcurrentHashMap\n" +
                        "Дата инициализации: " + collectionManager.getInitializationDate() + "\n" +
                        "Количество элементов: " + collectionManager.size());
    }

    private CommandResponse processShow() {
        List<Person> sorted = collectionManager.getCollection().values().stream()
                .sorted(Comparator.comparingInt(Person::getHeight))
                .toList();

        return new CommandResponse(true, "Коллекция (" + sorted.size() + " элементов)", sorted);
    }

    private CommandResponse processPrintHeight() {
        List<Integer> heights = collectionManager.getCollection().values().stream()
                .map(Person::getHeight)
                .sorted(Comparator.reverseOrder())
                .toList();

        String result = heights.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        return new CommandResponse(true, "Рост по убыванию: " + result);
    }

    private CommandResponse processUniqueEyeColor() {
        Set<Color> colors = collectionManager.getCollection().values().stream()
                .map(Person::getEyeColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new CommandResponse(true, "Уникальные цвета: " + colors);
    }
    private CommandResponse processInsert(CommandRequest request, int userId) {
        Person person = request.getPersonData();

        if (person == null) {
            return new CommandResponse(false, "Данные не переданы");
        }

        try {
            int generatedId = personDAO.insert(person, userId);
            person.setId(generatedId);
            person.setOwnerId(userId);
            collectionManager.getCollection().put(String.valueOf(generatedId), person);

            return new CommandResponse(true, "Добавлен ID=" + generatedId);
        } catch (SQLException e) {
            return new CommandResponse(false, "Ошибка БД: " + e.getMessage());
        }
    }

    private CommandResponse processUpdate(CommandRequest request, int userId) {
        String key = request.getArgument();
        Person newPerson = request.getPersonData();

        if (key == null || key.isEmpty()) {
            return new CommandResponse(false, "Ключ не указан");
        }

        if (newPerson == null) {
            return new CommandResponse(false, "Данные не переданы");
        }

        try {
            int personId = Integer.parseInt(key);

            if (!personDAO.checkOwnership(personId, userId)) {
                return new CommandResponse(false, "Нет прав на изменение этого объекта");
            }

            personDAO.update(newPerson, personId);

            newPerson.setId(personId);
            newPerson.setOwnerId(userId);
            collectionManager.getCollection().put(key, newPerson);

            return new CommandResponse(true, "Объект обновлен");

        } catch (NumberFormatException e) {
            return new CommandResponse(false, "Неверный формат ключа");
        } catch (SQLException e) {
            return new CommandResponse(false, e.getMessage());
        }
    }

    private CommandResponse processRemoveKey(CommandRequest request, int userId) {
        String key = request.getArgument();

        if (key == null || key.isEmpty()) {
            return new CommandResponse(false, "Ключ не указан");
        }

        try {
            int personId = Integer.parseInt(key);
            if (!personDAO.checkOwnership(personId, userId)) {
                return new CommandResponse(false, "Нет прав на удаление этого объекта");
            }
            personDAO.delete(personId);
            collectionManager.getCollection().remove(key);

            return new CommandResponse(true, "Объект удален");

        } catch (NumberFormatException e) {
            return new CommandResponse(false, "Неверный формат ключа");
        } catch (SQLException e) {
            return new CommandResponse(false, "Ошибка БД: " + e.getMessage());
        }
    }

    private CommandResponse processRemoveGreater(CommandRequest request, int userId) {
        Person threshold = request.getPersonData();

        if (threshold == null) {
            return new CommandResponse(false, "Объект для сравнения не передан");
        }

        try {
            List<String> keysToRemove = collectionManager.getCollection().entrySet().stream()
                    .filter(entry -> entry.getValue().getOwnerId() == userId)
                    .filter(entry -> entry.getValue().compareTo(threshold) > 0)
                    .map(Map.Entry::getKey)
                    .toList();

            for (String key : keysToRemove) {
                int personId = Integer.parseInt(key);
                personDAO.delete(personId);
                collectionManager.getCollection().remove(key);
            }

            return new CommandResponse(true, "Удалено элементов: " + keysToRemove.size());

        } catch (SQLException e) {
            return new CommandResponse(false, "Ошибка БД: " + e.getMessage());
        }
    }

    private CommandResponse processRemoveByNationality(CommandRequest request, int userId) {
        try {
            Country nationality = Country.valueOf(request.getArgument().toUpperCase());
            List<String> keys = collectionManager.getCollection().entrySet().stream()
                    .filter(e -> e.getValue().getOwnerId() == userId)
                    .filter(e -> e.getValue().getNationality() == nationality)
                    .map(Map.Entry::getKey)
                    .toList();

            for (String key : keys) {
                int personId = Integer.parseInt(key);
                personDAO.delete(personId);
                collectionManager.getCollection().remove(key);
            }

            return new CommandResponse(true, "Удалено: " + keys.size());

        } catch (IllegalArgumentException e) {
            return new CommandResponse(false, "Неверная национальность");
        } catch (SQLException e) {
            return new CommandResponse(false, e.getMessage());
        }
    }

    private CommandResponse processReplaceIfGreater(CommandRequest request, int userId) {
        String key = request.getArgument();
        Person newPerson = request.getPersonData();

        if (key == null || key.isEmpty()) {
            return new CommandResponse(false, "Ключ не указан");
        }

        if (newPerson == null) {
            return new CommandResponse(false, "Данные не переданы");
        }

        try {
            int personId = Integer.parseInt(key);

            if (!personDAO.checkOwnership(personId, userId)) {
                return new CommandResponse(false, "Нет прав на изменение этого объекта");
            }

            Person oldPerson = collectionManager.getCollection().get(key);
            if (oldPerson == null) {
                return new CommandResponse(false, "Объект не найден");
            }

            if (newPerson.compareTo(oldPerson) > 0) {
                personDAO.update(newPerson, personId);

                newPerson.setId(personId);
                newPerson.setOwnerId(userId);
                collectionManager.getCollection().put(key, newPerson);

                return new CommandResponse(true, "Объект заменен");
            } else {
                return new CommandResponse(false, "Новый объект не превышает старый");
            }

        } catch (NumberFormatException e) {
            return new CommandResponse(false, "Неверный формат ключа");
        } catch (SQLException e) {
            return new CommandResponse(false, e.getMessage());
        }
    }

    private CommandResponse processClear(int userId) {
        try {
            List<String> keysToRemove = collectionManager.getCollection().entrySet().stream()
                    .filter(e -> e.getValue().getOwnerId() == userId)
                    .map(Map.Entry::getKey)
                    .toList();

            int deleted = personDAO.deleteAllByOwner(userId);
            keysToRemove.forEach(collectionManager.getCollection()::remove);

            return new CommandResponse(true, "Удалено ваших объектов: " + deleted);

        } catch (SQLException e) {
            return new CommandResponse(false, e.getMessage());
        }
    }
    private CommandResponse processExecuteScript(CommandRequest request, int userId) {
        return processExecuteScriptWithTracking(request, new HashSet<>());
    }

    private CommandResponse processExecuteScriptWithTracking(
            CommandRequest request, Set<String> executedFiles) {

        String filename = request.getArgument();

        if (filename == null || filename.isEmpty()) {
            return new CommandResponse(false, "Не указан файл скрипта");
        }

        try {
            Path scriptPath = Path.of(filename).toAbsolutePath().normalize();
            String absolutePath = scriptPath.toString();

            if (executedFiles.contains(absolutePath)) {
                return new CommandResponse(false,
                        "Обнаружен циклический вызов скрипта: " + filename);
            }

            if (!Files.exists(scriptPath)) {
                return new CommandResponse(false, "Файл не найден: " + filename);
            }

            executedFiles.add(absolutePath);
            List<String> commands = Files.readAllLines(scriptPath);
            int executed = 0;
            StringBuilder result = new StringBuilder();

            for (String command : commands) {
                command = command.trim();
                if (command.isEmpty() || command.startsWith("#")) {
                    continue;
                }
                String[] parts = command.split("\\s+", 2);
                String cmdName = parts[0].toUpperCase();
                String arg = parts.length > 1 ? parts[1] : null;

                CommandType type;
                try {
                    type = CommandType.valueOf(cmdName);
                } catch (IllegalArgumentException e) {
                    result.append("Неизвестная команда: ").append(cmdName).append("\n");
                    continue;
                }
                if (type == CommandType.EXIT) {
                    result.append("Пропущена команда EXIT\n");
                    continue;
                }

                CommandRequest cmdRequest = new CommandRequest(
                        type,
                        arg,
                        null,
                        request.getUsername(),
                        request.getPassword()
                );

                CommandResponse response;
                if (type == CommandType.EXECUTE_SCRIPT) {
                    response = processExecuteScriptWithTracking(cmdRequest, executedFiles);
                } else {
                    response = execute(cmdRequest);
                }

                result.append(response.getMessage()).append("\n");
                executed++;
            }

            executedFiles.remove(absolutePath);

            return new CommandResponse(true,
                    "Скрипт выполнен. Выполнено команд: " + executed + "\n" + result);

        } catch (IOException e) {
            return new CommandResponse(false,
                    "Ошибка при чтении файла: " + e.getMessage());
        } catch (Exception e) {
            return new CommandResponse(false,
                    "Ошибка выполнения скрипта: " + e.getMessage());
        }
    }

    private String getHelpText() {
        return """
                Доступные команды:
                help : справка по командам
                info : информация о коллекции
                show : показать все элементы
                insert null {element} : добавить новый элемент
                update id {element} : обновить элемент по ID
                remove_key id : удалить элемент по ключу
                remove_greater {element} : удалить превышающие заданный
                clear : очистить СВОИ элементы
                print_unique_eye_color : уникальные цвета глаз
                print_field_descending_height : рост по убыванию
                remove_all_by_nationality nationality : удалить по национальности
                replace_if_greater id {element} : заменить если больше
                execute_script filename : выполнить скрипт
                history : история команд
                exit : завершить работу клиент
                Вы можете модифицировать только свои объекты.
                Все пользователи видят все объекты в коллекции.
                """;
    }
}