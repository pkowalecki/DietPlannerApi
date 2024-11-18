package pl.kowalecki.dietplannerrestapi.model.DTO;


import lombok.*;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientToBuyDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealHistoryDTO;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealHistoryResponse {
    List<String> mealNames;
    Double multiplier;
    List<MealDTO> meals;
    List<IngredientsToBuy> ingredientsToBuy;
}
