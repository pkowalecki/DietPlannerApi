package pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException;

public class MealsNotFoundException extends DataNotFoundException  {
    public MealsNotFoundException(String message) {
        super(message);
    }
}
