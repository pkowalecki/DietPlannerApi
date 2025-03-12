package pl.kowalecki.dietplannerrestapi.services.mealView;

import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealView;

import java.util.List;

public interface IMealViewService {

    List<MealView> getMealsToBoard(Long userId);
}
