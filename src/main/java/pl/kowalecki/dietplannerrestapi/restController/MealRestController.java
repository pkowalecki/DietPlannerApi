package pl.kowalecki.dietplannerrestapi.restController;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import pl.kowalecki.dietplannerrestapi.configuration.UserContextFilter;
import pl.kowalecki.dietplannerrestapi.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.*;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.services.IMealHistoryService;
import pl.kowalecki.dietplannerrestapi.services.IMealService;


import java.util.*;

@RequestMapping("/meal")
@RestController
@AllArgsConstructor
@Slf4j
public class MealRestController {

    private final IMealService mealService;
    private final IMealHistoryService IMealHistoryService;

    @GetMapping("/allMeal")
    public ResponseEntity<List<Meal>> getListMeal() {
        List<Meal> mealList = mealService.getAllMeals();
        return ResponseEntity.status(HttpStatus.OK).body(mealList);
    }

    @GetMapping(value = "/getMeal/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        if (mealService.getMealById(id) != null)
            return new ResponseEntity<>(mealService.getMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FIXME nie usuwamy, a ukrywamy ustawiając flagę isDeleted = true;
    @GetMapping(value = "/deleteMeal/{id}")
    public ResponseEntity<?> deleteMealById(@PathVariable Long id) {
        mealService.deleteMealById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/addMeal")
    public ResponseEntity<Void> addMeal(@RequestBody AddMealRequestDTO newMeal, ServerWebExchange exchange) {
        try {
            String userId = UserContextFilter.getUserId(exchange);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            mealService.addMeal(Long.parseLong(userId), newMeal);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            log.error("Entity not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("AddMeal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//@PostMapping("/generateFoodBoard")
//public ResponseEntity<ResponseBodyDTO> generateFoodBoard(@RequestBody FoodBoardPageDTO foodBoardPageDTO, HttpServletRequest request) {
//    Map<String, List<?>> responseData = new HashMap<>();
//    try {
//        List<Meal> mealList = mealRepository.findMealsByMealIdIn(foodBoardPageDTO.getMealIds());
//        List<IngredientsToBuy> ingredientToBuyDTOList = mealServiceImpl.getMealIngredientsFinalList(foodBoardPageDTO.getMealIds(), foodBoardPageDTO.getMultiplier());
//        responseData.put("mealList", mealList);
//        responseData.put("ingredientToBuyDTOList", ingredientToBuyDTOList);
//
//    } catch (Exception e) {
//        ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
//                .status(ResponseBodyDTO.ResponseStatus.ERROR)
//                .message("Meal not created")
//                .build();
//        log.error("generateFoodBoard: {}", e.getMessage());
//        e.printStackTrace();
//        return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
//    }
//
//
//    MealHistory mealHistory = MealHistory.builder()
//            .userId(Long.valueOf(12))
//            .public_id(UUID.randomUUID())
//            .mealsIds(foodBoardPageDTO.getMealIds().stream().map(String::valueOf).collect(Collectors.joining(",")))
//            .created(LocalDateTime.now())
//            .multiplier(foodBoardPageDTO.getMultiplier())
//            .build();
//    mealHistoryService.saveMealHistory(mealHistory);
//
//    ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
//            .status(ResponseBodyDTO.ResponseStatus.OK)
//            .data(responseData)
//            .build();
//    return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
//}
//    @PostMapping("/generateFoodRecipe")
//    public ResponseEntity<List<FoodDTO>> generateFoodRecipe(@RequestParam("ids") String ids, @RequestParam("multiplier") Double multiplier) {
//        List<Long> idsList = Arrays.stream(ids.split(","))
//                .map(Long::parseLong)
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(mealRepository.getMealRecipeFinalList(idsList, multiplier), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/getMealIngredientsList/{id}")
//    public ResponseEntity<List<Ingredient>> getMealIngredientsByMealId(@PathVariable Long id) {
//        if (mealServiceImpl.getMealIngredientsByMealId(id) != null)
//            return new ResponseEntity<>(mealServiceImpl.getMealIngredientsByMealId(id), HttpStatus.OK);
//        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @GetMapping(value = "/getIngredientMap")
//    public ResponseEntity<Map<IngredientUnit, List<String>>> getListIngredientUnit() {
//        return new ResponseEntity<>(mealServiceImpl.getIngredientUnitMap(), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/getMeasurementMap")
//    public ResponseEntity<Map<MeasurementType, List<String>>> getListMeasurementName() {
//        return new ResponseEntity<>(mealServiceImpl.getMeasurementTypeMap(), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/getMealsByUserId/{id}")
//    public ResponseEntity<List<Meal>> getMealsByUserId(@PathVariable Long id) {
//        if (id != null) return new ResponseEntity<>(mealServiceImpl.getMealByUserId(id), HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
    @GetMapping(value = "/getMealStarterPack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealStarterPackDTO> getMealStarterPack() {
            MealStarterPackDTO mealStarterPackDTO = mealService.buildStarterPack();
            return ResponseEntity.ok().body(mealStarterPackDTO);

    }
//
//    @GetMapping(value = "/ingredients", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResponseBodyDTO> getIngredients() {
//        try {
//            Map<String, List<IngredientName>> responseData = new HashMap<>();
//            List<IngredientNameDTO> ingredientNames = mealServiceImpl.getMealIngredientNames();
////        responseData.put("ingredients", ingredientNames);
//            ResponseBodyDTO response = ResponseBodyDTO.builder()
//                    .status(ResponseBodyDTO.ResponseStatus.OK)
//                    .data(responseData)
//                    .build();
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    private ResponseEntity<ResponseBodyDTO> getResponseError(Exception e) {
//        log.error("Error retrieving data", e);
//        ResponseBodyDTO errorResponse = ResponseBodyDTO.builder()
//                .status(ResponseBodyDTO.ResponseStatus.ERROR)
//                .data(Collections.singletonMap("error", "An error occurred"))
//                .build();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }
//
//    @PostMapping(value = "/getMealNamesById")
//    public ResponseEntity<ResponseBodyDTO> getMealNamesById(@RequestBody List<Long> mealIds) {
//        Map<String, List<String>> responseData = new HashMap<>();
//        try {
//            List<String> mealNames = mealServiceImpl.getMealNamesByIdList(mealIds);
//            if (!mealNames.isEmpty()) {
//                responseData.put("mealNames", mealNames);
//                ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
//                        .status(ResponseBodyDTO.ResponseStatus.OK)
//                        .data(responseData)
//                        .build();
//                return ResponseEntity.ok().body(responseBodyDTO);
//            }
//            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK, "No data", responseData, HttpStatus.OK);
//        } catch (Exception e) {
//            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.ERROR, "No data", responseData, HttpStatus.OK);
//        }
//    }
//
//    @GetMapping(value = "/getMealHistory")
//    public ResponseEntity<ResponseBodyDTO> getMealHistory(HttpServletRequest request) {
//        Map<String, List<MealHistoryDTO>> data = new HashMap<>();
////        Integer userId = authJwtUtils.getUserIdFromToken(request);
//        List<MealHistoryDTO> mealHistoryList = mealHistoryService.findMealHistoriesByUserId(Long.valueOf(12));
//        data.put("mealHistoryList", mealHistoryList);
//        return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK, data, HttpStatus.OK);
//
//
//    }
//
//    @PostMapping(value = "/getMealHistory")
//    public ResponseEntity<ResponseBodyDTO> getMealHistory(@RequestBody String id, HttpServletRequest request) {
////        UserDetails user = (UserDetails) authentication.getPrincipal();
//        MealHistory mealHistory = mealHistoryService.findMealHistoryByUUID(UUID.fromString(id));
//        if (mealHistory.getUserId().equals(2)) {
//            Map<String, MealHistoryResponse> data = new HashMap<>();
//            MealHistoryResponse response = new MealHistoryResponse();
//            String ids = mealHistory.getMealsIds();
//            List<Long> mealIds = Arrays.stream(ids.split(","))
//                    .map(Long::parseLong)
//                    .collect(Collectors.toList());
//            List<String> mealNames = mealServiceImpl.getMealNamesByIdList(mealIds);
//            response.setMealNames(mealNames);
//            response.setMultiplier(mealHistory.getMultiplier());
//            response.setMeals(getMealDTOList(mealIds));
//            List<IngredientsToBuy> ingredientToBuyDTOList = mealServiceImpl.getMealIngredientsFinalList(mealIds, mealHistory.getMultiplier());
//            response.setIngredientsToBuy(ingredientToBuyDTOList);
//            data.put("mealHistory", response);
//            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK, data, HttpStatus.OK);
//        } else {
//            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.ERROR, HttpStatus.UNAUTHORIZED);
//        }
//    }
//
//    private List<MealDTO> getMealDTOList(List<Long> mealIds) {
//        List<MealDTO> mealDTOList = new ArrayList<>();
//        for (Long mealId : mealIds) {
//            if (mealId == 0) continue;
//            Meal meal = mealServiceImpl.getMealById(mealId);
//            mealDTOList.add(MealDTO.builder()
//                    .additionDate(meal.getAdditionDate())
//                    .editDate(meal.getEditDate())
//                    .name(meal.getName())
//                    .description(meal.getDescription())
//                    .recipe(meal.getRecipe())
//                    .ingredients(setMealDTOIngredients(meal.getIngredients()))
//                    .notes(meal.getNotes())
//                    .mealTypes(meal.getMealTypes())
//                    .isDeleted(meal.isDeleted())
//                    .build());
//        }
//        return mealDTOList;
//    }
//
//    private List<IngredientTDTO> setMealDTOIngredients(List<Ingredient> ingredients) {
//        List<IngredientTDTO> ingredientDTOList = new ArrayList<>();
//        for (Ingredient ingredient : ingredients) {
//            ingredientDTOList.add(IngredientTDTO.builder()
//                    .name(setIngredientName(ingredient.getIngredientNameId()))
//                    .ingredientAmount(ingredient.getIngredientAmount())
//                    .ingredientUnit(ingredient.getIngredientUnit())
//                    .measurementValue(ingredient.getMeasurementValue())
//                    .measurementType(ingredient.getMeasurementType())
//                    .build());
//        }
//        return ingredientDTOList;
//    }
//
//    private IngredientNameDTO setIngredientName(IngredientName ingredientNameId) {
//        return IngredientNameDTO.builder()
//                .ingredientName(ingredientNameId.getName())
//                .ingredientBrand(ingredientNameId.getBrand())
//                .protein(ingredientNameId.getProtein())
//                .carbohydrates(ingredientNameId.getCarbohydrates())
//                .fat(ingredientNameId.getFat())
//                .kcal(ingredientNameId.getKcal())
//                .build();
//    }
//
}
