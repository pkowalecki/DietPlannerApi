package pl.kowalecki.dietplannerrestapi.services.meal;

import org.springframework.data.domain.Page;
import pl.kowalecki.dietplannerrestapi.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealDTO;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;
import pl.kowalecki.dietplannerrestapi.model.projection.MealProjection;

import java.util.List;

public interface IMealService {

    List<Meal> getAllMeals();
    Meal getMealById(Long id);
    void deleteMealById(Long id);
    void addOrUpdateMeal(Long userId, AddMealRequestDTO newMeal);
    List<Meal> findMealsByMealIdIn(List<Long> mealIds);
    MealStarterPackDTO buildStarterPack();
    List<IngredientsToBuy> getMealIngredientsFinalList(List<Long> ids, Double multiplier);
    List<String> getMealNamesByIdList(List<Long> mealIds);
    Page<MealProjection> findAllByPublic(boolean isPublic, int page, int size);
    MealDTO getMealDetailsByMealId(Long id, Long userId);
    Page<MealProjection> findAllByName(String name);
    List<String> getMealNamesByHistoryAndUserId(String pageId, Long userId);
    Page<MealProjection> getMeals(String userId, int page, int size, String mealType);

    List<MealProjection> findMealsByNameAndUserIdOrPublic(Long userId, String query);
}