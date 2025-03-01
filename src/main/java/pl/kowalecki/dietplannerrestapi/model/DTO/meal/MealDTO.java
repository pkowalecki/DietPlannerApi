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
    private List<IngredientTDTO> ingredients;
    private String notes;
    private List<MealTypeDTO> mealTypes;
    private Double portions;
    private boolean isDeleted;
    private boolean isPublic;
}
