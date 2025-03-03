package pl.kowalecki.dietplannerrestapi.services.meal.history;

import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealHistoryProjection;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;

import java.util.List;
import java.util.UUID;

public interface IMealHistoryService {

    void saveMealHistory(MealHistory mealHistory);

    List<MealHistoryProjection> findMealHistoriesByUserId(Long userId);

    MealHistoryProjection findMealHistoryByUUID(UUID id, Long userId);

}
