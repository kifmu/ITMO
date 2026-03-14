package modules;

import java.util.Scanner;
/**
 * Класс для управления консольным вводом и выводом.
 * @author kifmu
 */
public class ConsoleManager {
    private final Scanner scanner;

    public ConsoleManager() {
        this.scanner = new Scanner(System.in);
    }
    /**
     * Считывает строку из консоли.
     * @return введенная строка без пробелов по краям
     */
    public String readLine() {
        return scanner.nextLine().trim();
    }
    /**
     * Выводит текст в консоль.
     * @param text текст для вывода
     */
    public void write(String text) {
        System.out.println(text);
    }
    /**
     * Закрывает сканер.
     */
    public void close() {
        scanner.close();
    }
}