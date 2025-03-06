package pl.kowalecki.dietplannerrestapi.services.ingredient;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.exception.ObjectAlreadyExistsException;
import pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException.IngredientDetailsNotFoundException;
import pl.kowalecki.dietplannerrestapi.mapper.IngredientNameMapper;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.repository.IngredientNamesRepository;
import pl.kowalecki.dietplannerrestapi.utils.TextTools;

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

    //fixme refactor me plx
    @Override
    public void addOrEditIngredientDetails(Long userId, IngredientNameDTO ingredientNameDTO) {
        IngredientName ingredientName;
        String ingredientDetailName = TextTools.allToLower(ingredientNameDTO.ingredientName());
        String ingredientDetailBrand = TextTools.firstToUpper(ingredientNameDTO.ingredientBrand());
        if ("-1".equals(ingredientNameDTO.publicId().toString())){
            ingredientName = buildIngredientName(userId, ingredientNameDTO);
        }else {
            ingredientName = ingredientNamesRepository.findIngredientNameByPublicId(ingredientNameDTO.publicId())
                    .orElseThrow(() -> new IngredientDetailsNotFoundException("Nie znaleziono składnika o podanej nazwie"));

            ingredientName.setName(ingredientDetailName);
            ingredientName.setBrand(ingredientDetailBrand);
            ingredientName.setProtein(ingredientNameDTO.protein());
            ingredientName.setCarbohydrates(ingredientNameDTO.carbohydrates());
            ingredientName.setFat(ingredientNameDTO.fat());
            ingredientName.setKcal(ingredientNameDTO.kcal());
        }
        IngredientName existingIngredient = ingredientNamesRepository
                .findIngredientNameByNameContainingIgnoreCaseAndBrandContainingIgnoreCase(
                        ingredientDetailName, ingredientDetailBrand);

        if (existingIngredient != null
                && ingredientName.getName().equals(existingIngredient.getName())
                && ingredientName.getBrand().equals(existingIngredient.getBrand())) {
            throw new ObjectAlreadyExistsException("Ingredient with the same name and brand already exists.");
        }

        ingredientNamesRepository.save(ingredientName);
    }

    private IngredientName buildIngredientName(Long userId, IngredientNameDTO ingredientNameDTO) {
        return IngredientName.builder()
                .name(TextTools.allToLower(ingredientNameDTO.ingredientName()))
                .brand(TextTools.firstToUpper(ingredientNameDTO.ingredientBrand()))
                .protein(ingredientNameDTO.protein())
                .carbohydrates(ingredientNameDTO.carbohydrates())
                .fat(ingredientNameDTO.fat())
                .kcal(ingredientNameDTO.kcal())
                .userId(userId)
                .build();
    }
}
