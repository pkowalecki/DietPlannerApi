package pl.kowalecki.dietplannerrestapi.services;



import pl.kowalecki.dietplannerrestapi.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplannerrestapi.model.Meal;

import java.util.List;

public interface MealService {

    List<Meal> getAllMeals();
    Meal getMealById(Long id);
    boolean deleteMealById(Long id);
    void addMeal(Integer userId, AddMealRequestDTO newMeal);
    List<Meal> getMealByUserId(Long userId);
}
