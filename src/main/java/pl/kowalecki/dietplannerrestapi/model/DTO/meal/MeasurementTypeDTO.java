package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MeasurementTypeDTO {
    private int id;
    private String namePL;
    private String nameEN;

    public static List<MeasurementTypeDTO> buildMeasurementTypeDTO() {
        return Arrays.stream(MeasurementType.values()).map(
                        measurement -> MeasurementTypeDTO.builder()
                                .id(measurement.getId())
                                .namePL(measurement.getNamePL())
                                .nameEN(measurement.getNameEN())
                                .build())
                .collect(Collectors.toList());
    }
}
