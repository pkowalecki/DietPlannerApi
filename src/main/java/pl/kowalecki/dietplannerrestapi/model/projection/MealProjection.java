package pl.kowalecki.dietplannerrestapi.model.projection;

public interface MealProjection {
    Long getMealId();
    String getName();
    String getDescription();
    Long getUserId();
    boolean mealPublic();
}
