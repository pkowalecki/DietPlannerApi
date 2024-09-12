package pl.kowalecki.dietplannerrestapi.services;

import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;

import java.util.List;

public interface IngredientNamesService {

    List<IngredientNameDTO> searchByName(String name);
    boolean existsByName(String name);

    void addIngredientName(String newIngredientName);
}
