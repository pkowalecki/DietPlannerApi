package pl.kowalecki.dietplannerrestapi.exception;

public class MealsNotFoundException extends RuntimeException {
    public MealsNotFoundException(String message) {
        super(message);
    }
}
