package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;


@Builder
public record IngredientNameDTO(
        Long id,
        @JsonProperty("ingredientId")
        UUID publicId,
        String ingredientName,
        String ingredientBrand,
        double protein,
        double carbohydrates,
        double fat,
        double kcal,
        Long userId
) {

}
