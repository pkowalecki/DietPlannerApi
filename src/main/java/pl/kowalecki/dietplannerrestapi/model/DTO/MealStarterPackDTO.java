package pl.kowalecki.dietplannerrestapi.model.DTO;

import lombok.Builder;
import lombok.Data;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientUnitDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealTypeDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MeasurementTypeDTO;

import java.util.List;

@Data
@Builder
public class MealStarterPackDTO {
    List<IngredientNameDTO> ingredientNameList;
    List<MealTypeDTO> mealTypeList;
    List<IngredientUnitDTO> ingredientUnitList;
    List<MeasurementTypeDTO> measurementTypeList;
}
