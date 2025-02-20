package pl.kowalecki.dietplannerrestapi.services;

import org.springframework.data.domain.Page;
import pl.kowalecki.dietplannerrestapi.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealView;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;
import pl.kowalecki.dietplannerrestapi.model.projection.MealProjection;

import java.util.List;

public interface IMealService {

    List<Meal> getAllMeals();
    List<MealView> getAllMealsByUserId(Long userId);
    Meal getMealById(Long id);
    void deleteMealById(Long id);
    void addOrUpdateMeal(Long userId, AddMealRequestDTO newMeal);
    List<Meal> findMealsByMealIdIn(List<Long> mealIds);
    MealStarterPackDTO buildStarterPack();
    List<IngredientsToBuy> getMealIngredientsFinalList(List<Long> ids, Double multiplier);
    List<String> getMealNamesByIdList(List<Long> mealIds);
    Page<MealProjection> findAllByUserId(Long userId, int page, int size);
    Page<MealProjection> findAllByUserIdAndMealType(Long userId, String mealType, int page, int size);
    MealDTO getMealDetailsByMealAndUserId(Long id, Long userId);
    Page<MealProjection> findAllByNameAndUserId(String name, Long userId);
    List<String> getMealNamesByHistoryAndUserId(String pageId, Long userId);
}