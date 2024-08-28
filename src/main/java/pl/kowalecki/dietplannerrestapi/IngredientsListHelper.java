package pl.kowalecki.dietplannerrestapi;


import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;

import java.util.*;
import java.util.stream.Collectors;

public class IngredientsListHelper {

    public static List<Ingredient> prepareIngredientsList(List<Ingredient> ingredients, Double multiplier){
        Map<String, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient newIngredient: ingredients){
            Ingredient newIngredientToPut = new Ingredient();
            String ingredientNameKey = newIngredient.getIngredientNameId().getName() + newIngredient.getIngredientUnit().getShortName();
            if (ingredientMap.containsKey(ingredientNameKey)){
                Ingredient existingIngredient = ingredientMap.get(ingredientNameKey);
                if(newIngredient.getIngredientNameId().getName().equals(existingIngredient.getIngredientNameId().getName())){
                    //Tutaj lecimy z jednostkami danego składnika
                    if (newIngredient.getIngredientUnit().equals(existingIngredient.getIngredientUnit())){
                        existingIngredient.setIngredientAmount(existingIngredient.sumTotalAmount(getRoundedIngredientAmount(newIngredient.getIngredientAmount(),multiplier),existingIngredient.getIngredientAmount()));
                        existingIngredient.setIngredientUnit(existingIngredient.getIngredientUnit());
                    }
                    //Tutaj lecimy z rodzajem danego składnika
                    if(newIngredient.getMeasurementType().equals(existingIngredient.getMeasurementType())){
                        existingIngredient.setMeasurementValue(existingIngredient.sumTotalAmount(newIngredient.getMeasurementValue(), existingIngredient.getMeasurementValue()));
                        existingIngredient.setMeasurementType(existingIngredient.getMeasurementType());
                    }
                    newIngredientToPut = existingIngredient;
                    ingredientMap.put(ingredientNameKey, newIngredientToPut);
                }
            }else{
                newIngredient.setIngredientAmount(getRoundedIngredientAmount(newIngredient.getIngredientAmount(), multiplier));
                newIngredient.setMeasurementValue(newIngredient.getMeasurementValue()*multiplier);
                ingredientMap.put(ingredientNameKey, newIngredient);
            }
        }

        //Sortujemy mapkę, żeby dostać listę posortowaną alfabetycznie.
        Map<String, Ingredient> sortedMap = ingredientMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new ArrayList<>(sortedMap.values());
    }

    private static Double getRoundedIngredientAmount(Double ingredient, Double multiplier) {
        return (double)Math.round(((ingredient*multiplier)*100)/100);
    }

}
