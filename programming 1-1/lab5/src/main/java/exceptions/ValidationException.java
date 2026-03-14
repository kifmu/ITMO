package exceptions;

/**
 * Исключение, возникающее при ошибке валидации данных.
 * @author kifmu
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}