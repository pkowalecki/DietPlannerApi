package pl.kowalecki.dietplannerrestapi.model.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FoodBoardPageDTO {
    Double multiplier;
    List<Long> mealIds;
}
