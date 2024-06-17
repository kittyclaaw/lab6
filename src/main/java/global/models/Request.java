package global.models;

import java.io.*;

public class Request implements Serializable {
    private final String commandName;
    private String[] args;
    private Route route;

    public Request(String[] args) {
        this.commandName = args[0];
        this.args = args;
    }
    public Request(String[] args, Route route) {
        this.commandName = args[0];
        this.args = args;
        this.route = route;
    }

    public String getCommandName() { return commandName; }
    public String[] getArgs() { return args; }
    public Route getRoute() { return route; }

    @Override
    public String toString(){
        return "Request [commandName=" + commandName + ", args=(" + args[0] + "," + args[1] + ")]";
    }
}
