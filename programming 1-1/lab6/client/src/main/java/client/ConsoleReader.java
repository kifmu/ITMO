package client;

import java.util.Scanner;

/**
 * Класс для чтения ввода из консоли.
 * @author kifmu
 */
public class ConsoleReader {
    private final Scanner scanner;

    public ConsoleReader(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Считывает строку из консоли.
     * @return введённая строка (без пробелов)
     */
    public String readLine() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }
}