package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Builder
public record IngredientNameDTO(
        Long id,
        @JsonProperty("ingredientId")
        String publicId,
        String ingredientName,
        String ingredientBrand,
        double protein,
        double carbohydrates,
        double fat,
        double kcal,
        Long userId
) {

}
