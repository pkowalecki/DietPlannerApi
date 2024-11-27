package pl.kowalecki.dietplannerrestapi.services;

import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealHistoryDTO;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;

import java.util.List;
import java.util.UUID;

public interface IMealHistoryService {

    void saveMealHistory(MealHistory mealHistory);

    List<MealHistoryDTO> findMealHistoriesByUserId(Long userId);

    MealHistory findMealHistoryByUUID(UUID id);
}
