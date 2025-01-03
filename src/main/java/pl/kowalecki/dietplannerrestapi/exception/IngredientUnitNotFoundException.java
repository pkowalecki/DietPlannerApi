package pl.kowalecki.dietplannerrestapi.exception;

public class IngredientUnitNotFoundException extends RuntimeException {
    public IngredientUnitNotFoundException(String message) {
        super(message);
    }
}
