package commands;

import modules.CollectionHandler;
/**
 * Команда 'clear'. Очищает коллекцию.
 * @author kifmu
 */
public class Clear implements Command{
    CollectionHandler collectionHandler;

    public Clear(CollectionHandler collectionHandler) {
        this.collectionHandler = collectionHandler;
    }
    /**
     * Выполняет команду
     * @return Результат выполнения команды.
     */
    @Override
    public String execute(String[] arguments) {
        collectionHandler.clearCollection();
        return "Коллекция очищена.";
    }
    /**
     * Получить описание команды.
     * @return Описание команды.
     */
    @Override
    public String getDescription() {
        return "clear : очистить коллекцию";
    }
    /**
     * Получить название команды.
     * @return Название команды.
     */
    @Override
    public String getName() {
        return "clear";
    }
}
