package commands;

/**
 * Команда 'exit'. Завершает выполнение.
 * @author kifmu
 */
public class Exit implements Command {

    /**
     * Выполняет команду
     * @return Результат выполнения команды.
     */
    @Override
    public String execute(String[] arguments) {
        System.out.println("Программа завершена(без сохранения в файл)");
        System.exit(0);
        return null;
    }
    /**
     * Получить описание команды.
     * @return Описание команды.
     */
    @Override
    public String getDescription() {
        return "exit : завершить программу (без сохранения в файл)";
    }
    /**
     * Получить название команды.
     * @return Название команды.
     */
    @Override
    public String getName() {
        return "exit";
    }
}
