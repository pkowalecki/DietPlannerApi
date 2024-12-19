package pl.kowalecki.dietplannerrestapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientNameMapper {

    @Mapping(source = "name", target = "ingredientName")
    @Mapping(source = "brand", target = "ingredientBrand")
    IngredientNameDTO ingredientNameDTO(IngredientName ingredientName);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "ingredientName")
    @Mapping(source = "brand", target = "ingredientBrand")
    IngredientNameDTO ingredientNameDTOExcludedId(IngredientName ingredientName);

}
