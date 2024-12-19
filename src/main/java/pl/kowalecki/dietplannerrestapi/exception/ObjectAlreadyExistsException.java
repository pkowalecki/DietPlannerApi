package pl.kowalecki.dietplannerrestapi.exception;

public class ObjectAlreadyExistsException extends RuntimeException{

    public ObjectAlreadyExistsException(String message) {
        super(message);
    }

    public ObjectAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
