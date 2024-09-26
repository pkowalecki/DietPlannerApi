package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MealHistoryDTO{

    private Long id;
    private Long userId;
    private String mealsIds;
    private LocalDateTime created;
}
