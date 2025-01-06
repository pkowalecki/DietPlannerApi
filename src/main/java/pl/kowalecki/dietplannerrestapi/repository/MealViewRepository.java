package pl.kowalecki.dietplannerrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealView;

import java.util.List;

public interface MealViewRepository extends JpaRepository<MealView, Long> {

    List<MealView> findAllByUserId(Long userId);
}
