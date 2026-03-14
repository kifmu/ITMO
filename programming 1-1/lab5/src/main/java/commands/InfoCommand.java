package commands;

import modules.CollectionHandler;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 * @author kifmu
 */
public class InfoCommand implements Command {
    CollectionHandler collectionHandler;

    public InfoCommand(CollectionHandler collectionHandler) {
        this.collectionHandler = collectionHandler;
    }
    /**
     * Возвращает название команды.
     * @return название команды
     */
    @Override
    public String getName() {
        return "info";
    }
    /**
     * Возвращает описание команды.
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
    /**
     * Выполняет команду вывода информации о коллекции.
     * @param args аргументы команды
     * @return строка с информацией о коллекции
     */
    @Override
    public String execute(String[] args) {
        return "Тип коллекции:" + collectionHandler.getCollection().getClass().getSimpleName() +
                "\nДата инициализации: " + collectionHandler.getTime() +
                "\nКоличество элементов: " + collectionHandler.size();
    }
}