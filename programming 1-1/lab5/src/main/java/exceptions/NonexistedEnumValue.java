package exceptions;

/**
 * Исключение, возникающее при попытке использования несуществующего значения enum.
 * @author kifmu
 */
public class NonexistedEnumValue extends RuntimeException {
    public NonexistedEnumValue(String s) {
        super("Enum значение отсутствует.");
    }
}