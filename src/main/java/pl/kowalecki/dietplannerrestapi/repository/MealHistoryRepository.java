package pl.kowalecki.dietplannerrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealHistoryProjection;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MealHistoryRepository extends JpaRepository<MealHistory, Long> {

    @Query(value = "SELECT m.* FROM meal_history m WHERE m.user_id = :userId", nativeQuery = true)
    Optional<List<MealHistoryProjection>> findAllByUserId(Long userId);

    @Query(value = "SELECT m.* FROM meal_history m WHERE m.public_id = :publicId AND m.user_id = :userId", nativeQuery = true)
    Optional<MealHistoryProjection> findMealHistoryByPublicIdAndUserId(UUID publicId, Long userId);

}
