package server.commands;

import global.models.Request;
import global.models.Response;

/**
 * Команда выхода
 */
public class Exit extends Command  {

    public Exit(){
        super(Commands.EXIT,"завершить программу");
    }

    /**
     * Выполняет команду
     * @return возвращает сообщение об успешности выполнения команды
     */
    @Override
    public Response execute(Request request) {
        if (!(request.getArgs().length == 1)){
            return new Response("Неправильное количество аргументов!\nИспользование: '" + getCommandName() + "'");
        }
        System.exit(0);
        return new Response("завершение программы");
    }

}