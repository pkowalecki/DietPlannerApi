package pl.kowalecki.dietplannerrestapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.*;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;

@Mapper(componentModel = "spring")
public interface MealMapper {

    @Mapping(source = "ingredients", target = "ingredients")
    MealDTO mealToMealDTO(Meal meal);

    @Mapping(source = "ingredientNameId.name", target = "name.ingredientName")
    @Mapping(source = "ingredientNameId.brand", target = "name.ingredientBrand")
    @Mapping(source = "ingredientNameId.protein", target = "name.protein")
    @Mapping(source = "ingredientNameId.carbohydrates", target = "name.carbohydrates")
    @Mapping(source = "ingredientNameId.fat", target = "name.fat")
    @Mapping(source = "ingredientNameId.kcal", target = "name.kcal")
    @Mapping(source = "ingredientNameId", target = "ingredientNameId")
    IngredientTDTO ingredientToIngredientTDTO(Ingredient ingredient);


    default Long map(IngredientName ingredientName) {
        return ingredientName != null ? ingredientName.getId() : null;
    }

    @Mapping(target = "id", source = "measurementType.id")
    @Mapping(target = "namePL", source = "measurementType.namePL")
    @Mapping(target = "nameEN", source = "measurementType.nameEN")
    MeasurementTypeDTO mapMeasurementTypeToMeasurementTypeDTO(MeasurementType measurementType);

    @Mapping(target = "id", source = "ingredientUnit.id")
    @Mapping(target = "fullName", source = "ingredientUnit.fullNamePL")
    @Mapping(target = "shortName", source = "ingredientUnit.shortName")
    IngredientUnitDTO mapIngredientUnitToIngredientUnitDTO(IngredientUnit ingredientUnit);

    @Mapping(target = "id", source = "mealType.id")
    @Mapping(target = "mealTypePl", source = "mealType.mealTypePl")
    @Mapping(target = "mealTypeEn", source = "mealType.mealTypeEn")
    MealTypeDTO mapMealTypeToMealTypeDTO(MealType mealType);
}

