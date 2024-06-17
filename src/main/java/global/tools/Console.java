
package global.tools;

/**
 * Консоль для ввода команд
 */
public interface Console {
    void println(Object obj);
    void print(Object obj);
    String readln();
    void printError(Object obj);
    boolean isCanReadln();

}
