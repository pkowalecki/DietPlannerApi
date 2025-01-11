package pl.kowalecki.dietplannerrestapi.services;



import pl.kowalecki.dietplannerrestapi.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealView;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;

import java.util.List;

public interface IMealService {

    List<Meal> getAllMeals();
    List<MealView> getAllMealsByUserId(Long userId);
    Meal getMealById(Long id);
    void deleteMealById(Long id);
    void addMeal(Long userId, AddMealRequestDTO newMeal);
    List<Meal> findMealsByMealIdIn(List<Long> mealIds);
    MealStarterPackDTO buildStarterPack();
    List<IngredientsToBuy> getMealIngredientsFinalList(List<Long> ids, Double multiplier);
    List<String> getMealNamesByIdList(List<Long> mealIds);
}