package pl.kowalecki.dietplannerrestapi.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.repository.IngredientNamesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class IngredientNamesServiceImpl implements IngredientNamesService {

    @Autowired
    IngredientNamesRepository ingredientNamesRepository;


    @Override
    public List<IngredientNameDTO> searchByName(String name) {
        List<IngredientName> ingredientNames = ingredientNamesRepository.findByNameContainingIgnoreCase(name);
        return ingredientNames.stream()
                .map(ingredient ->{
                    IngredientNameDTO ingredientDTO = new IngredientNameDTO();
                    ingredientDTO.setId(ingredient.getId());
                    ingredientDTO.setName(ingredient.getName());
                    return ingredientDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return ingredientNamesRepository.existsByName(name);
    }

    @Override
    public void addIngredientName(String newIngredientName) {
        IngredientName ingredientName = new IngredientName();
        ingredientName.setName(newIngredientName);
        ingredientNamesRepository.save(ingredientName);
    }
}
