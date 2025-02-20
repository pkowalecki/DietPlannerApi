package pl.kowalecki.dietplannerrestapi.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ShoppingList {
    List<Long> mealIds;
    List<IngredientsToBuy> ingredients;
}
