package server.commands;

/**
 * Команды
 */
public enum Commands {
    ADD,
    CLEAR,
    EXIT,
    EXECUTE_SCRIPT,
    HELP,
    SAVE,
    HISTORY,
    INFO,
    PRINT_ASCENDING,
    PRINT_FIELD_ASCENDING_DISTANCE,
    REMOVE_BY_ID,
    REMOVE_HEAD,
    SHOW,
    UPDATE_BY_ID,
    NONAME_COMMAND;

    public boolean equals(String s) {
        return this.name().equalsIgnoreCase(s);
    }
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static Commands getCommandName(String commandName) {
        return switch (commandName) {
            case "add" -> ADD;
            case "clear" -> CLEAR;
            case "exit" -> EXIT;
            case "help" -> HELP;
            case "save" -> SAVE;
            case "history" -> HISTORY;
            case "info" -> INFO;
            case "print_ascending" -> PRINT_ASCENDING;
            case "print_field_ascending_distance" -> PRINT_FIELD_ASCENDING_DISTANCE;
            case "remove_by_id" -> REMOVE_BY_ID;
            case "remove_head" -> REMOVE_HEAD;
            case "show" -> SHOW;
            case "update_by_id" -> UPDATE_BY_ID;
            case "execute_script" -> EXECUTE_SCRIPT;
            default -> NONAME_COMMAND;
        };
    }
}
