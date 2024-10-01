package pl.kowalecki.dietplannerrestapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "meal_history")
public class MealHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID public_id;
    private Long userId;
    private String mealsIds;
    private LocalDateTime created;
    private Double multiplier;
}
