package pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException;

public class IngredientDetailsNotFoundException extends DataNotFoundException  {
    public IngredientDetailsNotFoundException(String message) {
        super(message);
    }
}
