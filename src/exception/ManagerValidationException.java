package exception;

public class ManagerValidationException extends RuntimeException{

    public ManagerValidationException(final String message) {
        super(message);
    }
}
