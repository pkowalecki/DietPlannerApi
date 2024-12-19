package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AddMealRequestDTO {
    private String mealName;
    private String description;
    private String recipe;
    private String notes;
    private List<IngredientDTO> ingredients;
    private List<Integer> mealTypes;
    private Double portions;
}
