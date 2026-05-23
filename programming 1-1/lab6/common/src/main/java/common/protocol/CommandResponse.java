package common.protocol;

import common.models.Person;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Объект ответа от сервера клиенту.
 */
public class CommandResponse implements Serializable {

    private final boolean success;
    private final String message;
    private final List<Person> data;

    public CommandResponse(boolean success, String message, List<Person> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public CommandResponse(boolean success, String message) {
        this(success, message, null);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Person> getData() { return data; }
}