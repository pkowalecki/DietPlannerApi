package pl.kowalecki.dietplannerrestapi.restController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientToBuyDTO;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplannerrestapi.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplannerrestapi.services.MealServiceImpl;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsImpl;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/api/auth/meal")
@RestController
@AllArgsConstructor
@Slf4j
public class MealRestController {

    private final AuthJwtUtils authJwtUtils;
    private final MealServiceImpl mealServiceImpl;

    @GetMapping( "/allMeal")
    public ResponseEntity<List<Meal>> getListMeal(){
        if (!mealServiceImpl.getAllMeals().isEmpty()) return new ResponseEntity<>(mealServiceImpl.getAllMeals(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/getMeal/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id){
        if (mealServiceImpl.getMealById(id) != null) return new ResponseEntity<>(mealServiceImpl.getMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping(value = "/deleteMeal/{id}")
    public ResponseEntity<?> deleteMealById(@PathVariable Long id) {
        if (mealServiceImpl.getMealById(id) != null)
            return new ResponseEntity<>(mealServiceImpl.deleteMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping( "/addMeal")
    public ResponseEntity<ResponseDTO> addMeal(@RequestBody AddMealRequestDTO newMeal){
        System.out.println("niu mil: " + newMeal);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();


            if (principal instanceof UserDetailsImpl) {
                UserDetailsImpl userDetails = (UserDetailsImpl) principal;
                String userEmail = userDetails.getEmail();
                String userName = userDetails.getUsername(); // Retrieve other details as needed
                // Perform your business logic with the user details
                System.out.println("User email: " + userEmail);
                System.out.println("User name: " + userName);
                mealServiceImpl.addMeal(userDetails.getId(), newMeal);
            }
        }

        ResponseDTO responseDTO = ResponseDTO.builder()
                .status(ResponseDTO.ResponseStatus.OK)
                .message("Meal created")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
    @PostMapping("/generateFoodBoard")
    public ResponseEntity<List<IngredientToBuyDTO>> generateFoodBoard(@RequestParam("ids") String ids, @RequestParam("multiplier") Double multiplier) {
        System.out.println("ids: " + ids);
        List<Long> idsList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return new ResponseEntity<>(mealServiceImpl.getMealIngredientsFinalList(idsList, multiplier), HttpStatus.OK);
    }
//    @PostMapping("/generateFoodRecipe")
//    public ResponseEntity<List<FoodDTO>> generateFoodRecipe(@RequestParam("ids") String ids, @RequestParam("multiplier") Double multiplier) {
//        List<Long> idsList = Arrays.stream(ids.split(","))
//                .map(Long::parseLong)
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(mealRepository.getMealRecipeFinalList(idsList, multiplier), HttpStatus.OK);
//    }

    @GetMapping(value = "/getMealIngredientsList/{id}")
    public ResponseEntity<List<Ingredient>> getMealIngredientsByMealId(@PathVariable Long id){
        if (mealServiceImpl.getMealIngredientsByMealId(id) != null) return new ResponseEntity<>(mealServiceImpl.getMealIngredientsByMealId(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/getIngredientMap")
    public ResponseEntity<Map<IngredientUnit,List<String>>> getListIngredientUnit(){
        return new ResponseEntity<>(mealServiceImpl.getIngredientUnitMap(), HttpStatus.OK);
    }
    @GetMapping(value = "/getMeasurementMap")
    public ResponseEntity<Map<MeasurementType, List<String>>> getListMeasurementName(){
        return new ResponseEntity<>(mealServiceImpl.getMeasurementTypeMap(), HttpStatus.OK);
    }

    @GetMapping(value = "/getMealsByUserId/{id}")
    public ResponseEntity<List<Meal>> getMealsByUserId(@PathVariable Long id){
        if (id != null) return new ResponseEntity<>(mealServiceImpl.getMealByUserId(id), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/getMealStarterPack")
    public ResponseEntity<ResponseDTO> getMealStarterPack(){
        try{
            Map<String, List<?>> responseData = new HashMap<>();

            List<IngredientName> ingredientNameList = mealServiceImpl.getMealIngredientNames();
            responseData.put("ingredientsNames", ingredientNameList);
            List<MealType> mealTypeList = Arrays.asList(MealType.values());
            responseData.put("mealTypes", mealTypeList);
            List<IngredientUnit> ingredientUnitList = Arrays.asList(IngredientUnit.values());
            responseData.put("ingredientUnits", ingredientUnitList);
            List<MeasurementType> measurementTypeList = Arrays.asList(MeasurementType.values());
            responseData.put("measurementTypes", measurementTypeList);
            ResponseDTO response = ResponseDTO.builder()
                    .status(ResponseDTO.ResponseStatus.OK)
                    .data(responseData)
                    .build();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        }catch (Exception e){
            return getResponseError(e);
        }

    }

    @GetMapping(value = "/ingredients")
    public ResponseEntity<ResponseDTO> getIngredients(){
        try {
            Map<String, List<IngredientName>> responseData = new HashMap<>();
            List<IngredientName> ingredientNames = mealServiceImpl.getMealIngredientNames();
            responseData.put("ingredients", ingredientNames);
            ResponseDTO response = ResponseDTO.builder()
                    .status(ResponseDTO.ResponseStatus.OK)
                    .data(responseData)
                    .build();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }catch (Exception e) {
            return getResponseError(e);
        }
    }

    private ResponseEntity<ResponseDTO> getResponseError(Exception e) {
        log.error("Error retrieving data", e);
        ResponseDTO errorResponse = ResponseDTO.builder()
                .status(ResponseDTO.ResponseStatus.ERROR)
                .data(Collections.singletonMap("error", "An error occurred"))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
}
