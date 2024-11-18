package pl.kowalecki.dietplannerrestapi.model.DTO.meal;


import lombok.*;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class IngredientTDTO {
    private IngredientNameDTO name;
    private Double ingredientAmount;
    private IngredientUnit ingredientUnit;
    private Double measurementValue;
    private MeasurementType measurementType;
    private Long ingredientNameId;
}