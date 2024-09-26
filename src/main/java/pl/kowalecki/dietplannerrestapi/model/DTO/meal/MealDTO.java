package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealDTO {
    private Long mealId;
    private LocalDateTime additionDate;
    private LocalDateTime editDate;
    private String name;
    private String description;
    private String recipe;
    private List<IngredientDTO> ingredients;
    private String notes;
    private List<MealTypeDTO> mealTypes;
    private boolean isDeleted;
}
