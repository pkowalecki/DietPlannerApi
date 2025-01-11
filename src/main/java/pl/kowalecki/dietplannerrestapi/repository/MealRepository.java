package pl.kowalecki.dietplannerrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kowalecki.dietplannerrestapi.model.Meal;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findMealsByUserId(Long id);

    @Query(value = "SELECT m.* FROM meals m " +
            "WHERE m.meal_id IN :mealIds",
            nativeQuery = true)
    List<Meal> findMealsByMealIdIn(List<Long> mealIds);

    @Query(value = "SELECT m.name from meals m WHERE m.meal_id=:mealId", nativeQuery = true)
    String getMealNameByMealId(Long mealId);
}
