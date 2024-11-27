package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class IngredientUnitDTO {
    private int id;
    private String fullName;
    private String shortName;

    public static List<IngredientUnitDTO> buildIngredientUnitDTO() {
        return Arrays.stream(IngredientUnit.values()).map(
                        ingredientUnit -> IngredientUnitDTO.builder()
                                .id(ingredientUnit.getId())
                                .fullName(ingredientUnit.getFullNamePL())
                                .shortName(ingredientUnit.getShortName())
                                .build())
                .collect(Collectors.toList());
    }
}
