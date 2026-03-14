package modules;

import commands.Command;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Класс для управления командами: регистрация, выполнение и хранение истории.
 * @author kifmu
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final List<String> commandHistory = new ArrayList<>();
    private static final int HISTORY_LIMIT = 9;
    /**
     * Выполняет команду по её названию.
     * @param commandName название команды
     * @param args аргументы команды
     * @return результат выполнения команды
     * @throws FileNotFoundException если команда требует файл, который не найден
     * @throws IllegalArgumentException если команда не зарегистрирована
     */
    public String execute(String commandName, String[] args) throws FileNotFoundException {
        Command command = commands.get(commandName);
        if (command == null) {
            throw new IllegalArgumentException("Данная команда не найдена: " + commandName);
        }
        String result = command.execute(args);
        if (result != null && !result.isEmpty()) {
            System.out.println(result);
        }
        return result;
    }
    /**
     * Регистрирует новую команду в менеджере.
     * @param name название команды
     * @param command объект команды, реализующий {@link Command}
     */
    public void register(String name, Command command) {
        commands.put(name, command);
    }
    /**
     * Возвращает карту всех зарегистрированных команд.
     * @return Map с командами
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
    /**
     * Возвращает список истории выполненных команд.
     * @return Список с историей команд
     */
    public List<String> getCommandHistory() {
        return commandHistory;
    }
    /**
     * Добавляет команду в историю.
     * Если история превышает лимит, удаляет самую старую команду.
     * @param command выполненная команда
     */
    public void addCommandHistory(String command) {
        commandHistory.add(command);
        if (commandHistory.size() > HISTORY_LIMIT) {
            commandHistory.remove(0);
        }
    }
}