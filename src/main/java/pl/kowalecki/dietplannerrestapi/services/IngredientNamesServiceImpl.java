package pl.kowalecki.dietplannerrestapi.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.exception.ObjectAlreadyExistsException;
import pl.kowalecki.dietplannerrestapi.mapper.IngredientNameMapper;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.repository.IngredientNamesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredientNamesServiceImpl implements IngredientNamesService {

    IngredientNamesRepository ingredientNamesRepository;
    IngredientNameMapper ingredientNameMapper;


    @Override
    public List<IngredientNameDTO> searchByName(String name) {
        List<IngredientName> ingredientNames = ingredientNamesRepository.findByNameContainingIgnoreCase(name);
        return ingredientNames.stream()
                .map(ingredientNameMapper::ingredientNameDTOExcludedId)
                .collect(Collectors.toList());
    }

    @Override
    public void addIngredientName(Long userId, IngredientNameDTO ingredientNameDTO) {
        IngredientName ingredientName = buildIngredientName(userId, ingredientNameDTO);
        if (ingredientNamesRepository.existsByNameAndBrand(ingredientName.getName(), ingredientName.getBrand())) {
            throw new ObjectAlreadyExistsException(
                    "Object found with name: " + ingredientName.getName() + " and brand: " + ingredientName.getBrand()
            );
        }ingredientNamesRepository.save(ingredientName);
    }

    private IngredientName buildIngredientName(Long userId, IngredientNameDTO ingredientNameDTO) {
        return IngredientName.builder()
                .name(ingredientNameDTO.ingredientName())
                .brand(ingredientNameDTO.ingredientBrand())
                .protein(ingredientNameDTO.protein())
                .carbohydrates(ingredientNameDTO.carbohydrates())
                .fat(ingredientNameDTO.fat())
                .kcal(ingredientNameDTO.kcal())
                .userId(userId)
                .build();
    }
}
