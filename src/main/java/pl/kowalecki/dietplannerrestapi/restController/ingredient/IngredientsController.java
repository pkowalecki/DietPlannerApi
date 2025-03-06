package pl.kowalecki.dietplannerrestapi.restController.ingredient;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;

import pl.kowalecki.dietplannerrestapi.services.ingredient.IngredientNamesService;

import java.util.List;


@RestController
@RequestMapping("/ingredient")
@AllArgsConstructor
public class IngredientsController {

    private IngredientNamesService ingredientService;

    @GetMapping("/ingredientNames/search")
    public ResponseEntity<List<IngredientNameDTO>> searchIngredients(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam("query") String query) {
        List<IngredientNameDTO> ingredients = ingredientService.searchByName(query);
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/ingredientNames/addOrEditIngredientDetails")
    public ResponseEntity<Void> addIngredient(
            @RequestBody IngredientNameDTO newIngredientName,
            @RequestHeader("X-User-Id") String userId
    ) {
        ingredientService.addOrEditIngredientDetails(Long.valueOf(userId), newIngredientName);
        return ResponseEntity.ok().build();
    }

}
