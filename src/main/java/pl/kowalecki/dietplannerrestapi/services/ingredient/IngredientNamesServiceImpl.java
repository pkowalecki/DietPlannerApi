package pl.kowalecki.dietplannerrestapi.services.ingredient;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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


    public List<IngredientNameDTO> searchByName(String name) {
        Pageable pageable = PageRequest.of(0, 20);
        List<IngredientName> ingredientNames = ingredientNamesRepository.findByNameContainingIgnoreCase(name, pageable);
        return ingredientNames.stream()
                .map(ingredientNameMapper::ingredientNameDTOExcludedId)
                .collect(Collectors.toList());
    }

    @Override
    public void addIngredientName(Long userId, IngredientNameDTO ingredientNameDTO) {
        IngredientName ingredientName = buildIngredientName(userId, ingredientNameDTO);
        IngredientName existingIngredientName = ingredientNamesRepository.findIngredientNameByNameAndBrand(ingredientName.getName(), ingredientName.getBrand());
        if (existingIngredientName!=null) {
            existingIngredientName.setProtein(ingredientName.getProtein());
            existingIngredientName.setCarbohydrates(ingredientName.getCarbohydrates());
            existingIngredientName.setFat(ingredientName.getFat());
            existingIngredientName.setKcal(ingredientName.getKcal());
            ingredientNamesRepository.save(existingIngredientName);
        }else{
            ingredientNamesRepository.save(ingredientName);
        }

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
