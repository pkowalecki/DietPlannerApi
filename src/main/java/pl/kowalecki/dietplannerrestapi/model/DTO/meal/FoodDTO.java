package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FoodDTO {

    String name;
    String recipe;
    String description;
    List<Ingredient> ingredients;
}
