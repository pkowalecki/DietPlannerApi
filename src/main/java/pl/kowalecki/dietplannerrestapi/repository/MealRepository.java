package pl.kowalecki.dietplannerrestapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.projection.MealProjection;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query(value = "SELECT m.* FROM meals m " +
            "WHERE m.meal_id IN :mealIds",
            nativeQuery = true)
    List<Meal> findMealsByMealIdIn(List<Long> mealIds);

    @Query(value = "SELECT m.name from meals m WHERE m.meal_id=:mealId", nativeQuery = true)
    String getMealNameByMealId(Long mealId);

    Page<MealProjection> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT m FROM Meal m WHERE m.userId = :userId AND :mealType MEMBER OF m.mealTypes")
    Page<MealProjection> findAllByUserIdAndMealTypes(@Param("userId") Long userId, @Param("mealType") MealType mealType, Pageable pageable);



}
