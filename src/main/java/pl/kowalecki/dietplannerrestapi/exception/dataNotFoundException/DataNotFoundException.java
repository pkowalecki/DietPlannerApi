package pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
