package pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum IngredientUnit {
    GRAM("gram", "g"),
    KILOGRAM("kilogramów", "kg"),
    MILILITR("mililitrów", "ml"),
    LITR("litrów", "l");

    String fullName;
    String shortName;

    IngredientUnit(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public static IngredientUnit getByShortName(String shortName) {
        for (IngredientUnit ingredientUnit : values()) {
            if (ingredientUnit.getShortName().equals(shortName)) return ingredientUnit;
        }
        return null;
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
                ingredientValues.add(ingredientUnit.getFullName());
                ingredientValues.add(ingredientUnit.getShortName());
            }
            ingredientUnitMap.put(ingredientUnit, ingredientValues);
        }
        return ingredientUnitMap;
    }


}
