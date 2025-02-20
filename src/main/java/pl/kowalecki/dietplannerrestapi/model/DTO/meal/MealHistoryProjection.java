package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MealHistoryProjection {
    UUID getPublic_id();
    Long getUserId();
    String getMealsIds();
    LocalDateTime getCreated();
    Double getMultiplier();
}
