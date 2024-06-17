package global.tools;

import java.util.Objects;
import java.util.Scanner;

/**
 * Класс реализации методов интерфеса консоли
 */

public class StandartConsole implements Console {
    private static Scanner fileScanner = null;
    private static final Scanner defScanner = new Scanner(System.in);

    /**
     * консоль делает System.out.println(obj)
     */

    @Override
    public void println(Object obj){
        System.out.println(obj);
    }

    /**
     * консоль делает System.out.print(obj)
     */

    @Override
    public void print(Object obj){
        System.out.print(obj);
    }

    /**
     * Консоль считывает команду которую мы вводим
     */

    @Override
    public String readln() {
        return Objects.requireNonNullElse(fileScanner, defScanner).nextLine().trim();
    }

   /**
     * консоль выводит ошибку
     */
    @Override
    public void printError(Object obj){
        System.out.println(obj);
    }

    /**
     * консоль выводит две колонки с командой и пояснением к ней
     */

    @Override
    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }

}