package server.commands;

import global.models.*;
import server.managers.CollectionManager;
import java.util.List;


/**
 * Команда очистки коллекции
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super(Commands.CLEAR, "очистить коллекцию");
        this.collectionManager = collectionManager;

    }

    /**
     * Выполняет команду
     * @return возвращает сообщение об успешности выполнения команды
     */
    @Override
    public Response execute(Request request){
        if(request.getArgs().length != 1){
            return new Response("Неправильное количество аргументов!\nИспользование: '" + getCommandName() + "'");
        }

        collectionManager.clear();
        collectionManager.update();
        return new Response("Коллекция очищена");
    }
}
