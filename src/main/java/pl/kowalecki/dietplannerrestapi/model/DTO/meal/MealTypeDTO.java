package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealTypeDTO {
    private String mealTypePl;
    private String mealTypeEn;
}
