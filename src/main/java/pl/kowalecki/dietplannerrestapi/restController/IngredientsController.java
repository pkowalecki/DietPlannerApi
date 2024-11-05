package pl.kowalecki.dietplannerrestapi.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientNameDTO;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.services.IApiService;
import pl.kowalecki.dietplannerrestapi.services.IngredientNamesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ingredient")

public class IngredientsController {

    @Autowired
    private IngredientNamesService ingredientService;
    @Autowired
    private IApiService apiService;

    @GetMapping("/ingredientNames/search")
    public ResponseEntity<ResponseBodyDTO> searchIngredients(@RequestParam("query") String query) {
        List<IngredientNameDTO> ingredients = ingredientService.searchByName(query);
        Map<String, List<IngredientNameDTO>> data = new HashMap<>();
        data.put("ingredientNames", ingredients);
        return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK, "", data, HttpStatus.OK);
    }

    @PostMapping("/ingredientNames/ingredient")
    public ResponseEntity<ResponseBodyDTO> addIngredient(@RequestBody IngredientNameDTO newIngredientName) {
        if (newIngredientName == null || newIngredientName.getIngredientName().isEmpty()) {
            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK, "", null, HttpStatus.NO_CONTENT);
        }
        boolean ingredientExists = ingredientService.existsByName(newIngredientName.getIngredientName());
        if(ingredientExists){
            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.ERROR, "Produkt o podanej nazwie już istnieje",  HttpStatus.CONFLICT);
        }
        IngredientName ingredientName = new IngredientName();
        ingredientName.setName(newIngredientName.getIngredientName());
        ingredientName.setBrand(newIngredientName.getIngredientBrand());
        ingredientName.setProtein(newIngredientName.getProtein());
        ingredientName.setCarbohydrates(newIngredientName.getCarbohydrates());
        ingredientName.setFat(newIngredientName.getFat());
        ingredientName.setKcal(newIngredientName.getKcal());
        ingredientService.addIngredientName(ingredientName);
        return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK,
                "Produkt został dodany",
                HttpStatus.CREATED);
    }

}
