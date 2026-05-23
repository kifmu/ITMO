package common.protocol;

public enum CommandType {
    HELP, INFO, SHOW, HISTORY,

    INSERT, UPDATE, REPLACE_IF_GREATER,

    REMOVE_KEY, REMOVE_GREATER, REMOVE_ALL_BY_NATIONALITY, CLEAR,

    PRINT_UNIQUE_EYE_COLOR, PRINT_FIELD_DESCENDING_HEIGHT,

    EXECUTE_SCRIPT,

    SAVE,

    EXIT;

    public boolean isServerOnly() {
        return this == SAVE;
    }

    public boolean isClientOnly() {
        return this == EXIT;
    }
}