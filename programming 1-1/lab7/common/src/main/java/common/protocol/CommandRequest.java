package common.protocol;

import common.models.Person;
import java.io.Serializable;

/**
 * Объект запроса от клиента к серверу.
 */
public class CommandRequest implements Serializable {

    private final CommandType type;
    private final String argument;
    private final Person personData;
    private final String username;
    private final String password;

    public CommandRequest(CommandType type, String argument, Person personData) {
        this(type, argument, personData, null, null);
    }
    public CommandRequest(CommandType type, String argument, Person personData,
                          String username, String password) {
        this.type = type;
        this.argument = argument;
        this.personData = personData;
        this.username = username;
        this.password = password;
    }

    public CommandType getType() {
        return type;
    }

    public String getArgument() {
        return argument;
    }

    public Person getPersonData() {
        return personData;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}