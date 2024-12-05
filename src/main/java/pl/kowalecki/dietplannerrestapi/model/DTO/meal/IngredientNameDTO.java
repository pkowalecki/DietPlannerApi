package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;


@Builder
public record IngredientNameDTO(
        Long id,
        String ingredientName,
        String ingredientBrand,
        double protein,
        double carbohydrates,
        double fat,
        double kcal,
        Long userId
) {

}
