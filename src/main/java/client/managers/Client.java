package client.managers;

import global.models.*;
import global.tools.*;
import global.tools.Console;
import server.commands.Commands;
import server.managers.CommandManager;
import static client.tools.Ask.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * Класс для подключения клиента к серверу
 */

public class Client {
    private static final Console console = new StandartConsole();
    private final CommandManager commandManager;
    private static InetSocketAddress address;
    private static SocketChannel channel = null;
    private static Selector selector;
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public Client(String host, int port, CommandManager commandManager) throws IOException {
        address = new InetSocketAddress(host, port);
        selector = Selector.open();
        this.commandManager = commandManager;
    }

    public void start() throws AskBreak, IOException, ClassNotFoundException, InterruptedException {
        connectToServer();

        while (console.isCanReadln()) {

            var input = console.readln().split(" ", 2);
            var commandName = input[0];
            var request = new Request(input);

            try {
                switch (Commands.valueOf(commandName.toUpperCase())) {
                    case EXIT:
                        console.println("Завершение работы");
                        try {
                            channel.close();
                            var command = commandManager.getCommand(commandName);
                            command.execute(request);
                        } catch (IOException | ClassNotFoundException | InterruptedException e) {
                            console.printError("Ошибка при закрытии соединения");
                        }
                        break;
                    case ADD, UPDATE_BY_ID:
                        var route = askRoute();
                        request = new Request(input, route);
                        sendRequest(request);
                        break;
                    case EXECUTE_SCRIPT:
                        var command = commandManager.getCommand(commandName);
                        command.execute(request);
                        break;
                    default:
                        sendRequest(request);
                }
            } catch (IllegalArgumentException e) {
                console.printError("Такой команды не существует. Введите команду 'help' или почините свои кривые руки");
            }
        }
    }

    private void connectToServer() throws InterruptedException {
        while (!isConnected()) {
            try {
                channel = SocketChannel.open();
                channel.configureBlocking(false);
                channel.connect(address);
                channel.register(selector, SelectionKey.OP_CONNECT);

                while (true) {
                    selector.select();
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while (keys.hasNext()) {
                        SelectionKey key = keys.next();
                        keys.remove();
                        if (key.isConnectable()) {
                            channel = (SocketChannel) key.channel();
                            if (channel.finishConnect()) {
                                key.interestOps(SelectionKey.OP_READ);
                                console.println("Подключение к серверу установлено");
                                return;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                console.printError("Неудачная попытка подключения");
                Thread.sleep(5000);
            }
        }
    }

    public static void sendRequest(Request request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(request);
        oos.flush();

        byte[] requestData = baos.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(requestData);
        channel.write(buffer);
        forkJoinPool.submit(() -> {
            try {
                console.println(getAnswer());
            } catch (IOException | ClassNotFoundException e) {
                console.printError("Ошибка при получении ответа");
            }
        });
    }

    public static Response getAnswer() throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(10000);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        channel.configureBlocking(false);
        selector.wakeup();
        channel.register(selector, SelectionKey.OP_READ);

        while (true) {
            selector.wakeup();
            int readyChannels = selector.select();
            if (readyChannels == 0) continue;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isReadable()) {
                    channel = (SocketChannel) key.channel();
                    int bytesRead = channel.read(buffer);

                    if (bytesRead == -1) {
                        key.cancel();
                        channel.close();
                        return null;
                    }

                    buffer.flip();
                    baos.write(buffer.array(), 0, bytesRead);
                    buffer.clear();

                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
                    return (Response) ois.readObject();
                }
            }
        }
    }

    private boolean isConnected() {
        return channel != null && channel.isConnected();
    }
}
