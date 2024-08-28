package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IngredientDTO {
    private Double ingredientAmount;
    private String ingredientUnit;
    private Double measurementValue;
    private String measurementType;
    private Long ingredientNameId;
}
