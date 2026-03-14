package commands;

import modules.CommandManager;

/**
 * Команда 'history'. Выводит последние 9 выполненных команд.
 * @author kifmu
 */
public class History implements Command {
    CommandManager commandManager;

    public History(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * Выполняет команду вывода истории выполненных команд.
     * @param arguments аргументы команды
     * @return строка с перечислением последних 9 выполненных команд
     */
    @Override
    public String execute(String[] arguments) {
        return commandManager.getCommandHistory().toString();
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "history : вывести последние 9 команд (без их аргументов)";
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "history";
    }
}