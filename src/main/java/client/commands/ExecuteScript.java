package client.commands;

import client.managers.Client;
import global.models.*;
import global.tools.Console;
import global.tools.StandartConsole;
import server.commands.Command;
import server.commands.Commands;
import server.managers.CommandManager;

import static server.commands.Commands.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

/**
 * Команда выполнения скрипта из файла
 */

public class ExecuteScript extends Command {
    private final Console console = new StandartConsole();
    private final CommandManager commandManager;
    private static final Stack<File> fileStack = new Stack<>();

    public ExecuteScript(CommandManager commandManager) {
        super(EXECUTE_SCRIPT, "Исполнить скрипт из указанного файла");
        this.commandManager = commandManager;
    }

    public Response execute(Request scriptRequest) throws IOException, ClassNotFoundException, InterruptedException {
        try {
            var fileName = scriptRequest.getArgs()[1];
            var file = new File(fileName);
            if (fileStack.isEmpty() || !fileStack.contains(file)) fileStack.add(file);

            var br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            String line;
            var lines = new String[9];
            while ((line = br.readLine()) != null) {
                var args = line.split(" ");
                var commandName = lines[0];
                Request request;
                switch (Commands.valueOf(commandName)) {
                    case ADD, UPDATE_BY_ID:
                        lines = br.lines().toArray(String[]::new);
                        request = getRequest(lines, args);
                        break;
                    default:
                        request = new Request(args);
                        break;
                }
                Client.sendRequest(request);
            }
            fileStack.pop();
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            console.printError("Не указано название файла название файла");
        } catch (FileNotFoundException e) {
            console.printError("Файла не существует.");
        }
        return null;
    }

    private static Request getRequest(String[] lines, String[] args) {
        var routeName = lines[0];
        var coordinates = new Coordinates(Float.parseFloat(lines[1]), Float.parseFloat(lines[2]));
        var locationFrom = new Location(Long.parseLong(lines[3]), Integer.parseInt(lines[4]), lines[5]);
        var locationTo = new Location(Long.parseLong(lines[6]), Integer.parseInt(lines[7]), lines[8]);
        var distance = Float.parseFloat(lines[9]);
        var route = new Route(routeName, coordinates, locationFrom, locationTo, distance);
        return new Request(args, route);
    }
}