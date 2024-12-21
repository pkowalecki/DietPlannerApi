package pl.kowalecki.dietplannerrestapi.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;

import java.util.List;


@Repository
public interface IngredientNamesRepository extends JpaRepository<IngredientName, Long> {

    List<IngredientName> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByNameAndBrand(String name, String brand);
    IngredientName findIngredientNameByNameAndBrand(String name, String brand);
}
