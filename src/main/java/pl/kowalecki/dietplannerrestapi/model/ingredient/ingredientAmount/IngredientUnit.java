package pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException.IngredientUnitNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum IngredientUnit {
    GRAM(1,"gram", "EN[gram]", "g"),
    KILOGRAM(2,"kilogramów", "EN[kilogramów]","kg"),
    MILILITR(3,"mililitrów", "EN[mililitrów]","ml"),
    LITR(4, "litrów", "EN[litrów]", "l");

    private int id;
    private String fullNamePL;
    private String fullNameEN;
    private String shortName;


    public static IngredientUnit getByShortName(String shortName) {
        for (IngredientUnit ingredientUnit : values()) {
            if (ingredientUnit.getShortName().equals(shortName)) return ingredientUnit;
        }
        throw new IngredientUnitNotFoundException("Ingredient Unit with name: "+ shortName +" not found!");
    }

    public static IngredientUnit getById(int id) {
        for (IngredientUnit ingredientUnit : values()) {
            if (ingredientUnit.getId() == id) return ingredientUnit;
        }
        throw new IngredientUnitNotFoundException("Ingredient Unit with id: "+ id +" not found!");
    }

    public static List<String> getAllIngredientNames(){
        List<String> ingredientNames = new ArrayList<>();
        for (IngredientUnit ingredientUnit: values()){
            ingredientNames.add(ingredientUnit.getShortName());
        }
        return ingredientNames;

    }

    public static Map<IngredientUnit, List<String>> getIngredientUnitMap(){
        Map<IngredientUnit, List<String>> ingredientUnitMap = new HashMap<>();
        for(IngredientUnit ingredientUnit : values()){
            List<String> ingredientValues = new ArrayList<>();
            if (!ingredientUnitMap.containsKey(ingredientUnit)) {
                ingredientValues.add(ingredientUnit.getFullNamePL());
                ingredientValues.add(ingredientUnit.getShortName());
            }
            ingredientUnitMap.put(ingredientUnit, ingredientValues);
        }
        return ingredientUnitMap;
    }


}
