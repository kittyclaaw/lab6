package server.commands;

import global.models.Request;
import global.models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.managers.CollectionManager;
import java.io.IOException;

/**
 * команда сохраняющая коллекцию в файл
 */
public class Save extends Command{
    private final CollectionManager collectionManager;
    private final Logger LOGGER = LoggerFactory.getLogger(Save.class);
    public Save(CollectionManager collectionManager) {
        super(Commands.SAVE, "Сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws IOException, ClassNotFoundException, InterruptedException {
        if (request.getArgs().length != 1) {
            return new Response(
                    "Неправильное количество аргументов!\nИспользование: " +
                            "'" + getCommandName() + "'");
        }
        try {
            int id = collectionManager.getCollection().get(0).getId();
            collectionManager.saveCollection();
            LOGGER.info("save {}", id);
            return new Response("Коллекция успешно сохранена");
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        return new Response( "Коллекция пуста!");
    }
}