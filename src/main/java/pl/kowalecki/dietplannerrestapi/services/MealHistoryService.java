package pl.kowalecki.dietplannerrestapi.services;

import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealHistoryDTO;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;

import java.util.List;

public interface MealHistoryService{

    void saveMealHistory(MealHistory mealHistory);

    List<MealHistoryDTO> findMealHistoriesByUserId(Long userId);
}
