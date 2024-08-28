package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealBaseDTO {
    private List<IngredientName> ingredientNameList;
    private List<MealType> mealTypeList;
    private List<IngredientUnit> ingredientUnitList;
    private List<MeasurementType> measurementTypeList;
}
