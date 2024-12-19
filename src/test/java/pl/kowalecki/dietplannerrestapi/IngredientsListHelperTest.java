package pl.kowalecki.dietplannerrestapi;

import org.junit.jupiter.api.Test;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IngredientsListHelperTest {

    @Test
    void finalIngredientListShouldReturnGoodValues(){
        Meal meal = new Meal();
        IngredientName ingredientName1 = new IngredientName(0L, "Masło", "Marka", 0, 0, 0,0, 1L);
        IngredientName ingredientName2 = new IngredientName(1L, "Masło", "Marka", 0, 0, 0,0,1L);
        IngredientName ingredientName3 = new IngredientName(2L, "Pomidor", "Marka", 0, 0, 0,0,1L);

        Ingredient ingredient1 =  new Ingredient(1L,3.8,IngredientUnit.GRAM,1.3,MeasurementType.LYZECZKA,
                ingredientName1,
                meal);
        Ingredient ingredient2 =  new Ingredient(2L,1.3,IngredientUnit.GRAM,1.3,MeasurementType.LYZECZKA,
                ingredientName2,
                meal);
        Ingredient ingredient3 =  new Ingredient(3L,1.3,IngredientUnit.GRAM,1.3,MeasurementType.LYZECZKA,
                ingredientName3,
                meal);

        List<Ingredient> lista = IngredientsListHelper.prepareIngredientsList(
                List.of(ingredient1, ingredient2, ingredient3), 1.0
        );

        for (Ingredient ingredient : lista) {
            System.out.println(ingredient.getIngredientNameId().getName() + " " + ingredient.getIngredientAmount());
        }
    }

    @Test
    void testShoppingList(){
        Meal meal = new Meal();
        IngredientName ingredientName1 = new IngredientName(0L, "Masło", "Mlekowida", 0, 0, 0,0,1L);
        IngredientName ingredientName2 = new IngredientName(1L, "Masło", "Osełkowe", 0, 0, 0,0,1L);
        IngredientName ingredientName3 = new IngredientName(2L, "Pomidor", "Kiść", 0, 0, 0,0,1L);
        IngredientName ingredientName4 = new IngredientName(3L, "Pomidor", "Kiść", 0, 0, 0,0,1L);

        Ingredient ingredient1 = new Ingredient(1L, 50.0, IngredientUnit.GRAM, 1.0, MeasurementType.SZTUKA, ingredientName1, meal);
        Ingredient ingredient2 = new Ingredient(2L, 50.0, IngredientUnit.GRAM, 1.0, MeasurementType.SZTUKA, ingredientName2, meal);
        Ingredient ingredient3 = new Ingredient(3L, 50.0, IngredientUnit.GRAM, 1.0, MeasurementType.SZTUKA, ingredientName3, meal);
        Ingredient ingredient4 = new Ingredient(3L, 50.0, IngredientUnit.GRAM, 1.0, MeasurementType.PORCJA, ingredientName4, meal);

        List<Ingredient> ingredients = List.of(ingredient1, ingredient2, ingredient3, ingredient4);

        IngredientsListHelper service = new IngredientsListHelper();
        List<IngredientsToBuy> shoppingList1 = service.generateShoppingList(ingredients, 2.0);
        List<IngredientsToBuy> shoppingList2 = service.generateShoppingList(ingredients, 1.0);

        System.out.println("Lista 1: ");
        for (IngredientsToBuy ingredientsToBuy : shoppingList1) {
            System.out.println(ingredientsToBuy.getName() + ", " + ingredientsToBuy.getBrand());
            System.out.println(ingredientsToBuy.getIngredientAmount() + ingredientsToBuy.getIngredientUnit());
            System.out.println(ingredientsToBuy.getMeasurementList());
        }

        System.out.println("Lista 2: ");
        for (IngredientsToBuy ingredientsToBuy : shoppingList2) {
            System.out.println(ingredientsToBuy.getName() + ", " + ingredientsToBuy.getBrand());
            System.out.println(ingredientsToBuy.getIngredientAmount() + ingredientsToBuy.getIngredientUnit());
            System.out.println(ingredientsToBuy.getMeasurementList());
        }

    }

}