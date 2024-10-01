package pl.kowalecki.dietplannerrestapi.model.DTO;


import lombok.*;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealHistoryDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealHistoryResponse {
    List<String> mealNames;
    Double multiplier;
    List<MealDTO> meals;
}
