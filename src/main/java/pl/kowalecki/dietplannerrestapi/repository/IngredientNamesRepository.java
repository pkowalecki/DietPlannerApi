package pl.kowalecki.dietplannerrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;


@Repository
public interface IngredientNamesRepository extends JpaRepository<IngredientName, Long> {
}
