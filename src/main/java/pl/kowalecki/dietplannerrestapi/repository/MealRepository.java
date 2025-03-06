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
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {

    @Query(value = "SELECT m.* FROM meals m " +
            "WHERE m.meal_id IN :mealIds",
            nativeQuery = true)
    List<Meal> findMealsByMealIdIn(List<Long> mealIds);

    @Query(value = "SELECT m.name from meals m WHERE m.meal_id=:mealId", nativeQuery = true)
    String getMealNameByMealId(Long mealId);

    Page<MealProjection> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.ingredients WHERE m.mealId = :id")
    Optional<Meal> getMealById(@Param("id") Long id);

    Optional<Meal> findMealByMealIdAndUserId(Long mealId, Long userId);

    Page<MealProjection> findMealByNameContainingIgnoreCase(@Param("mealName") String mealName, Pageable pageable);

    Page<MealProjection> findAllByMealPublic(boolean mealPublic, Pageable pageable);

    Page<MealProjection> findAllByUserIdOrMealPublic(Long userId, boolean mealPublic, Pageable pageable);

    @Query("SELECT m FROM Meal m WHERE m.userId = :userId AND :mealType MEMBER OF m.mealTypes")
    Page<MealProjection> findAllByUserIdAndMealTypes(@Param("userId") Long userId, @Param("mealType") MealType mealType, Pageable pageable);

    Page<MealProjection> findAllByNameContainingIgnoreCaseAndUserIdOrNameContainingIgnoreCaseAndMealPublic(
            String name1, Long userId, String name2, boolean mealPublic, Pageable pageable);

}
