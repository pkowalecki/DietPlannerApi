package pl.kowalecki.dietplannerrestapi.services;



import pl.kowalecki.dietplannerrestapi.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplannerrestapi.model.Meal;

import java.util.List;

public interface IMealService {

    List<Meal> getAllMeals();
    Meal getMealById(Long id);
    void deleteMealById(Long id);
    void addMeal(Long userId, AddMealRequestDTO newMeal);
    List<Meal> getMealByUserId(Long userId);
    List<Meal> findMealsByMealIdIn(List<Long> mealIds);
    MealStarterPackDTO buildStarterPack();
}