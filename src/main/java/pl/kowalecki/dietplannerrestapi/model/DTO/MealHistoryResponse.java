package pl.kowalecki.dietplannerrestapi.model.DTO;


import lombok.*;
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
    List<IngredientsToBuy> ingredientsToBuy;
    String documentId;
}
