package pl.kowalecki.dietplannerrestapi.model.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsToBuy {
    private String name;
    private String brand;
    private double ingredientAmount;
    private String ingredientUnit;
    private List<Measurement> measurementList;
}
