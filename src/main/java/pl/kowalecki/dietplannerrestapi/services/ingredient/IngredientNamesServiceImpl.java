package pl.kowalecki.dietplannerrestapi.services.ingredient;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException.IngredientDetailsNotFoundException;
import pl.kowalecki.dietplannerrestapi.mapper.IngredientNameMapper;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.repository.IngredientNamesRepository;
import pl.kowalecki.dietplannerrestapi.utils.TextTools;

import java.util.List;
import java.util.UUID;
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
    public void addOrEditIngredientDetails(Long userId, IngredientNameDTO ingredientNameDTO) {
        String normalizedName = TextTools.allToLower(ingredientNameDTO.ingredientName());
        String normalizedBrand = ingredientNameDTO.ingredientBrand() == null
                || ingredientNameDTO.ingredientBrand().isEmpty()? "" : TextTools.firstToUpper(ingredientNameDTO.ingredientBrand());

        IngredientName ingredientName = isNewIngredient(ingredientNameDTO, normalizedName, normalizedBrand)
                ? createNewIngredient(userId, ingredientNameDTO, normalizedName, normalizedBrand)
                : updateExistingIngredient(ingredientNameDTO, normalizedName, normalizedBrand);
        
        ingredientNamesRepository.save(ingredientName);
    }

    private IngredientName updateExistingIngredient(IngredientNameDTO dto,
                                                    String normalizedName, String normalizedBrand) {
        IngredientName ingredientName = ingredientNamesRepository
                .findIngredientNameByPublicId(UUID.fromString(dto.publicId()))
                .orElseThrow(() -> new IngredientDetailsNotFoundException("Nie znaleziono składnika o podanej nazwie"));

        updateIngredientFields(ingredientName, dto, normalizedName, normalizedBrand);
        return ingredientName;
    }

    private void updateIngredientFields(IngredientName ingredient, IngredientNameDTO dto,
                                        String normalizedName, String normalizedBrand) {
        ingredient.setName(normalizedName);
        ingredient.setBrand(normalizedBrand);
        ingredient.setProtein(dto.protein());
        ingredient.setCarbohydrates(dto.carbohydrates());
        ingredient.setFat(dto.fat());
        ingredient.setKcal(dto.kcal());
    }

    private IngredientName createNewIngredient(Long userId, IngredientNameDTO dto,
                                               String normalizedName, String normalizedBrand) {
        return IngredientName.builder()
                .name(normalizedName)
                .brand(normalizedBrand)
                .protein(dto.protein())
                .carbohydrates(dto.carbohydrates())
                .fat(dto.fat())
                .kcal(dto.kcal())
                .userId(userId)
                .build();
    }

    private boolean isNewIngredient(IngredientNameDTO ingredientNameDTO, String normalizedName, String normalizedBrand) {
        return "-1".equals(ingredientNameDTO.publicId()) && !checkIfDuplicated(normalizedName, normalizedBrand);
    }

    private boolean checkIfDuplicated(String name, String brand) {
        IngredientName existingIngredient = ingredientNamesRepository
                .findIngredientNameByNameContainingIgnoreCaseAndBrandContainingIgnoreCase(name, brand);

        return existingIngredient != null &&
                name.equals(existingIngredient.getName()) &&
                brand.equals(existingIngredient.getBrand());
    }
}
