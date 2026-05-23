package server;

import common.models.Color;
import common.models.Country;
import common.models.Person;
import common.protocol.CommandRequest;
import common.protocol.CommandResponse;
import common.protocol.CommandType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CommandExecutor {
    private final CollectionManager collectionManager;

    public CommandExecutor(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public CommandResponse execute(CommandRequest request) {
        try {
            CommandType type = request.getType();

            if (type.isServerOnly()) {
                return new CommandResponse(false, "Команда доступна только серверу");
            }

            return switch (type) {
                case HELP -> new CommandResponse(true, getHelpText());
                case INFO -> processInfo();
                case SHOW -> processShow();
                case INSERT -> processInsert(request);
                case UPDATE -> processUpdate(request);
                case REMOVE_KEY -> processRemoveKey(request);
                case REMOVE_GREATER -> processRemoveGreater(request);
                case PRINT_FIELD_DESCENDING_HEIGHT -> processPrintHeight();
                case PRINT_UNIQUE_EYE_COLOR -> processUniqueEyeColor();
                case REMOVE_ALL_BY_NATIONALITY -> processRemoveByNationality(request);
                case CLEAR -> { collectionManager.clear();
                    yield new CommandResponse(true, "Коллекция очищена"); }
                case HISTORY -> new CommandResponse(true, "История на клиенте");
                case REPLACE_IF_GREATER -> processReplaceIfGreater(request);
                case EXECUTE_SCRIPT -> processExecuteScript(request);
                case EXIT, SAVE -> new CommandResponse(false, "Недопустимая команда");
            };

        } catch (Exception e) {
            return new CommandResponse(false, e.getMessage());
        }
    }

    private CommandResponse processExecuteScript(CommandRequest request) {
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
                        "Обнаружен циклический вызов скрипта " + filename);
            }
            if (!Files.exists(scriptPath)) {
                return new CommandResponse(false, "Файл не найден " + filename);
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
                    result.append("Неизвестная команда ").append(cmdName).append("\n");
                    continue;
                }

                CommandRequest cmdRequest = new CommandRequest(type, arg, null);
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
                    "Выполнено команд " + executed + "\n" + result);

        } catch (Exception e) {
            return new CommandResponse(false, e.getMessage());
        }
    }
    private CommandResponse processReplaceIfGreater(CommandRequest request) {
        String key = request.getArgument();
        Person newPerson = request.getPersonData();

        if (key == null || key.isEmpty()) {
            return new CommandResponse(false, "Ключ не указан");
        }
        if (newPerson == null) {
            return new CommandResponse(false, "Данные объекта не переданы");
        }

        Person oldPerson = collectionManager.getCollection().get(key);
        if (oldPerson == null) {
            return new CommandResponse(false, "Элемент с ключом " + key + " не найден");
        }

        if (newPerson.compareTo(oldPerson) > 0) {
            newPerson.setId(Integer.parseInt(key));
            collectionManager.getCollection().put(key, newPerson);
            return new CommandResponse(true, "Элемент с ключом " + key + " заменён");
        } else {
            return new CommandResponse(false,
                    "Новый элемент не превышает старый, замена не выполнена");
        }
    }
    private CommandResponse processShow() {
        List<Person> sorted = collectionManager.getCollection().values().stream()
                .sorted(Comparator.comparingInt(Person::getHeight))
                .collect(Collectors.toList());

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

    private CommandResponse processInsert(CommandRequest request) {
        Person person = request.getPersonData();

        if (person == null) {
            return new CommandResponse(false, "Данные объекта не переданы");
        }

        boolean added = collectionManager.add(person);
        if (added) {
            return new CommandResponse(true,
                    "Элемент добавлен с ID = " + person.getId());
        } else {
            return new CommandResponse(false, "Ошибка добавления");
        }
    }

    private CommandResponse processRemoveGreater(CommandRequest request) {
        Person threshold = request.getPersonData();
        if (threshold == null) {
            return new CommandResponse(false, "Объект для сравнения не передан");
        }
        List<String> keysToRemove = collectionManager.getCollection().entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(threshold) > 0)
                .map(Map.Entry::getKey)
                .toList();

        keysToRemove.forEach(collectionManager::removeByKey);

        return new CommandResponse(true, "Удалено элементов: " + keysToRemove.size());
    }

    private CommandResponse processInfo() {
        return new CommandResponse(true,
                "Тип: LinkedHashMap\n" +
                        "Дата инициализации: " + collectionManager.getInitializationDate() + "\n" +
                        "Количество элементов: " + collectionManager.size());
    }

    private CommandResponse processRemoveKey(CommandRequest request) {
        String key = request.getArgument();
        if (key == null || key.isEmpty()) {
            return new CommandResponse(false, "Ключ не указан");
        }
        boolean removed = collectionManager.removeByKey(key);
        return new CommandResponse(removed, removed ? "Удалено" : "Ключ не найден");
    }

    private CommandResponse processUpdate(CommandRequest request) {
        // Аналогично INSERT, но с поиском по ключу
        return new CommandResponse(true, "Обновлено");
    }

    private CommandResponse processUniqueEyeColor() {
        Set<Color> colors = collectionManager.getCollection().values().stream()
                .map(Person::getEyeColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new CommandResponse(true, "Уникальные цвета: " + colors);
    }

    private CommandResponse processRemoveByNationality(CommandRequest request) {
        Country nationality;
        try {
            nationality = Country.valueOf(request.getArgument().toUpperCase());
        } catch (Exception e) {
            return new CommandResponse(false, "Неверная национальность");
        }

        List<String> keys = collectionManager.getCollection().entrySet().stream()
                .filter(e -> e.getValue().getNationality() == nationality)
                .map(Map.Entry::getKey)
                .toList();

        keys.forEach(collectionManager::removeByKey);
        return new CommandResponse(true, "Удалено: " + keys.size());
    }

    private String getHelpText() {
        return "Доступные команды:\n" +
                "help : вывести справку по доступным командам\n " +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "insert null {element} : добавить новый элемент с заданным ключом\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_key null : удалить элемент из коллекции по его ключу\n" +
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "clear : очистить коллекцию\n" +
                "print_unique_eye_color : вывести уникальные значения поля eyeColor всех элементов в коллекции\n" +
                "print_field_descending_height : вывести значения поля height всех элементов в порядке убывания\n" +
                "remove_all_by_nationality nationality : удалить из коллекции все элементы, значение поля nationality которого эквивалентно заданному\n" +
                "history : вывести последние 9 команд (без их аргументов)\n" +
                "exit : завершить программу (без сохранения в файл)";
    }
}