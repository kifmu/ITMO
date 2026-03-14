package commands;

import java.io.FileNotFoundException;

/**
 * Интерфейс команды с именем и описанием
 * @author kifmu
 */
public interface Command {
    String execute(String[] arguments) throws FileNotFoundException;
    String getDescription();
    String getName();
}
