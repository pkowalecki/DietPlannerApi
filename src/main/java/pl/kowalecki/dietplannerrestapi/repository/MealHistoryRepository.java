package pl.kowalecki.dietplannerrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;

import java.util.List;

public interface MealHistoryRepository extends JpaRepository<MealHistory, Long> {

    @Query(value = "SELECT m.* FROM meal_history m WHERE m.user_id = :id", nativeQuery = true)
    List<MealHistory> findAllByUserId(Long userId);
}
