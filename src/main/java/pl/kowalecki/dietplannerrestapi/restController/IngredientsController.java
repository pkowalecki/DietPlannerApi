package pl.kowalecki.dietplannerrestapi.restController;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;

import pl.kowalecki.dietplannerrestapi.services.IngredientNamesService;

import java.util.List;


@RestController
@RequestMapping("/ingredient")

public class IngredientsController {

    @Autowired
    private IngredientNamesService ingredientService;

    @GetMapping("/ingredientNames/search")
    public ResponseEntity<List<IngredientNameDTO>> searchIngredients(@RequestParam("query") String query) {
        List<IngredientNameDTO> ingredients = ingredientService.searchByName(query);
        return ResponseEntity.ok(ingredients);
    }

    @PostMapping("/ingredientNames/ingredient")
    public ResponseEntity<Void> addIngredient(
            @RequestBody IngredientNameDTO newIngredientName,
            @RequestHeader("X-User-Id") String userId
    ) {
        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ingredientService.addIngredientName(Long.valueOf(userId), newIngredientName);
        return ResponseEntity.ok().build();
    }

}
