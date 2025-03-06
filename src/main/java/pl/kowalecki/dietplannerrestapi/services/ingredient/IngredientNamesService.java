package pl.kowalecki.dietplannerrestapi.services.ingredient;

import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;

import java.util.List;

public interface IngredientNamesService {

    List<IngredientNameDTO> searchByName(String name);
    void addOrEditIngredientDetails(Long userId, IngredientNameDTO newIngredientName);
}
