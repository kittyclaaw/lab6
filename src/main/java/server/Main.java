package server;

import global.tools.Console;
import global.tools.StandartConsole;
import server.commands.*;
import server.managers.TCPServer;
import server.managers.CollectionManager;
import server.managers.CommandManager;
import server.tools.CSVparser;

import static server.commands.Commands.*;
import java.io.IOException;


public class Main {
    private final static int PORT = 4129;
    private final static String HOST = "localhost";

    public static void main(String[] args) {
        String dataFileName = args[0]; // Имя файла из аргумента
        Console console = new StandartConsole();
        CSVparser csVparser = new CSVparser(dataFileName, console);
        CollectionManager collectionManager = new CollectionManager(csVparser);
        CommandManager commandManager = new CommandManager();
        if (!collectionManager.loadCollection()) {
            System.err.println("Error: Collection could not be loaded!");
            return;
        }

        commandManager.register(ADD, new Add(collectionManager));
        commandManager.register(CLEAR, new Clear(collectionManager));
        commandManager.register(SHOW, new Show(collectionManager));
        commandManager.register(HELP, new Help(commandManager));
        commandManager.register(SAVE, new Save(collectionManager));
        commandManager.register(UPDATE_BY_ID, new UpdateById(collectionManager));
        commandManager.register(HISTORY, new History(commandManager));
        commandManager.register(EXIT, new Exit());
        commandManager.register(INFO, new Info(collectionManager));
        commandManager.register(REMOVE_HEAD, new RemoveHead(collectionManager));
        commandManager.register(PRINT_ASCENDING, new PrintAscending(collectionManager));
        commandManager.register(REMOVE_BY_ID, new RemoveById(collectionManager));
        commandManager.register(PRINT_FIELD_ASCENDING_DISTANCE, new PrintFieldAsсendingDistance(collectionManager));

        try {
            new TCPServer(HOST, PORT, commandManager).start();
        } catch (IOException | ClassNotFoundException ignored) {}
    }
}