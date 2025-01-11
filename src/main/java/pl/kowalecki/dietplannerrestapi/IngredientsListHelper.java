package pl.kowalecki.dietplannerrestapi;


import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Measurement;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;

import java.util.*;
import java.util.stream.Collectors;

public class IngredientsListHelper {

    public static List<Ingredient> prepareIngredientsList(List<Ingredient> ingredients, Double multiplier){
        Map<String, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient ingredient: ingredients){
            Ingredient newIngredientToPut = new Ingredient();
            String ingredientNameKey = ingredient.getIngredientNameId().getName();
            if (ingredientMap.containsKey(ingredientNameKey)){
                Ingredient existingIngredient = ingredientMap.get(ingredientNameKey);
                if(ingredient.getIngredientNameId().getName().equals(existingIngredient.getIngredientNameId().getName())){
                    //Tutaj lecimy z jednostkami danego składnika
                    if (ingredient.getIngredientUnit().equals(existingIngredient.getIngredientUnit())){
                        existingIngredient.setIngredientAmount(existingIngredient.sumTotalAmount(getRoundedIngredientAmount(ingredient.getIngredientAmount(),multiplier),existingIngredient.getIngredientAmount()));
                        existingIngredient.setIngredientUnit(existingIngredient.getIngredientUnit());
                    }
//                    //Tutaj lecimy z rodzajem danego składnika
//                    if(ingredient.getMeasurementType().equals(existingIngredient.getMeasurementType())){
//                        existingIngredient.setMeasurementValue(existingIngredient.sumTotalAmount(ingredient.getMeasurementValue(), existingIngredient.getMeasurementValue()));
//                        existingIngredient.setMeasurementType(existingIngredient.getMeasurementType());
//                    }
                    newIngredientToPut = existingIngredient;
                    ingredientMap.put(ingredientNameKey, newIngredientToPut);
                }
            }else{
//                ingredient.setIngredientAmount(getRoundedIngredientAmount(ingredient.getIngredientAmount(), multiplier));
                ingredient.setIngredientAmount(ingredient.getIngredientAmount());
//                ingredient.setMeasurementValue(ingredient.getMeasurementValue()*multiplier);
                ingredientMap.put(ingredientNameKey, ingredient);
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

    private static final Map<IngredientUnit, Double> MASS_CONVERSION = Map.of(
            IngredientUnit.GRAM, 1.0,
            IngredientUnit.KILOGRAM, 1000.0
    );
    private static final Map<IngredientUnit, Double> VOLUME_CONVERSION = Map.of(
            IngredientUnit.MILILITR, 1.0,
            IngredientUnit.LITR, 1000.0
    );


    public List<IngredientsToBuy> generateShoppingList(List<Ingredient> ingredients, double multiplier) {
        Map<String, IngredientsToBuy> groupedIngredients = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            String normalizedName = ingredient.getIngredientNameId().getName().trim().toLowerCase();
            String brand = ingredient.getIngredientNameId().getBrand();

            String key = normalizedName + (brand != null ? "_" + brand : "") + "_" + ingredient.getIngredientUnit();

            double scaledAmount = ingredient.getIngredientAmount();
            if (isMassUnit(ingredient.getIngredientUnit())) {
                scaledAmount = normalizeToBaseUnit(scaledAmount, ingredient.getIngredientUnit(), MASS_CONVERSION);
            } else if (isVolumeUnit(ingredient.getIngredientUnit())) {
                scaledAmount = normalizeToBaseUnit(scaledAmount, ingredient.getIngredientUnit(), VOLUME_CONVERSION);
            }

            Measurement measurement = new Measurement(
                    ingredient.getMeasurementValue() != null ? ingredient.getMeasurementValue() : 0.0,
                    ingredient.getMeasurementType() != null ? ingredient.getMeasurementType().toString().toLowerCase() : null
            );

            groupedIngredients.merge(key,
                    new IngredientsToBuy(
                            normalizedName,
                            brand,
                            scaledAmount,
                            getBaseUnit(ingredient.getIngredientUnit()),
                            new ArrayList<>(List.of(measurement))
                    ),
                    this::mergeIngredients);
        }

        return groupedIngredients.values().stream()
                .peek(ingredient -> {
                    ingredient.setIngredientAmount(ingredient.getIngredientAmount() * multiplier);
                    ingredient.getMeasurementList().forEach(m -> m.setMeasurementAmount(m.getMeasurementAmount() * multiplier));
                })
                .sorted(Comparator.comparing(IngredientsToBuy::getName))
                .collect(Collectors.toList());
    }

    private IngredientsToBuy mergeIngredients(IngredientsToBuy existing, IngredientsToBuy toAdd) {
        existing.setIngredientAmount(existing.getIngredientAmount() + toAdd.getIngredientAmount());

        Map<String, Measurement> measurementsMap = existing.getMeasurementList().stream()
                .collect(Collectors.toMap(
                        Measurement::getMeasurementUnit,
                        m -> m,
                        (m1, m2) -> {
                            m1.setMeasurementAmount(m1.getMeasurementAmount() + m2.getMeasurementAmount());
                            return m1;
                        }
                ));

        toAdd.getMeasurementList().forEach(newMeasurement -> {
            measurementsMap.merge(
                    newMeasurement.getMeasurementUnit(),
                    newMeasurement,
                    (existingMeasurement, addedMeasurement) -> {
                        existingMeasurement.setMeasurementAmount(existingMeasurement.getMeasurementAmount() + addedMeasurement.getMeasurementAmount());
                        return existingMeasurement;
                    }
            );
        });

        existing.setMeasurementList(new ArrayList<>(measurementsMap.values()));
        return existing;
    }

    private boolean isMassUnit(IngredientUnit unit) {
        return MASS_CONVERSION.containsKey(unit);
    }

    private boolean isVolumeUnit(IngredientUnit unit) {
        return VOLUME_CONVERSION.containsKey(unit);
    }

    private double normalizeToBaseUnit(double amount, IngredientUnit unit, Map<IngredientUnit, Double> conversionMap) {
        return amount * conversionMap.getOrDefault(unit, 1.0);
    }

    private String getBaseUnit(IngredientUnit unit) {
        if (isMassUnit(unit)) return "g";
        if (isVolumeUnit(unit)) return "ml";
        return unit.name().toLowerCase();
    }


    public static List<Ingredient> prepareIngredients(List<Ingredient> ingredients, Double multiplier){
        IngredientsToBuy ingredientsToBuy = new IngredientsToBuy();
        HashMap<String, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            Ingredient ingredientToPut = new Ingredient();
            //Sprawdzamy, czy mapa zawiera dany składnik
            if (ingredientMap.containsKey(ingredient.getIngredientNameId().getName())){
                ingredientToPut = ingredientMap.get(ingredient.getIngredientNameId().getName());
                System.out.println("mom kopie: " + ingredient.getIngredientNameId().getName());
                //Sprawdzamy, czy składnik ma taką samą miarę. Gramy/ML czy inne
                    if (ingredient.getIngredientUnit().equals(ingredientMap.get(ingredient.getIngredientNameId().getName()).getIngredientUnit())){
                        //Mamy taką samą miarę to możemy sumować
                        ingredientToPut.setIngredientAmount(ingredient.getIngredientAmount() + ingredientToPut.getIngredientAmount());
                        ingredientMap.put(ingredientToPut.getIngredientNameId().getName(),ingredientToPut);
                    }
                    else{
                        //Mamy inne miary, to nie możemy sumować.
                        ingredientMap.put(ingredientToPut.getIngredientNameId().getName(), ingredientToPut);
                    }
            }else {
                System.out.println("nie mam kopi");
                ingredientMap.put(ingredient.getIngredientNameId().getName(), ingredient);
            }
        }





        Map<String, Ingredient> sortedMap = ingredientMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return new ArrayList<>(sortedMap.values());
    }

}
