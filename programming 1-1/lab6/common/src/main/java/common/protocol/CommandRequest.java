package common.protocol;

import common.models.Person;

import java.io.Serializable;

/**
 * Объект запроса от клиента к серверу.
 * Все команды передаются как объекты этого класса.
 */
public class CommandRequest implements Serializable {

    private final CommandType type;
    private final String argument;
    private final Person personData;

    public CommandRequest(CommandType type, String argument, Person personData) {
        this.type = type;
        this.argument = argument;
        this.personData = personData;
    }

    public CommandType getType() { return type; }
    public String getArgument() { return argument; }
    public Person getPersonData() { return personData; }
}