package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientNameDTO {

    private Long id;
    private String ingredientName;
    private String ingredientBrand;
    private int protein;
    private int carbohydrates;
    private int fat;
    private int kcal;
}
