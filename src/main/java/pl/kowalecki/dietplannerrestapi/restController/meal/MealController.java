package pl.kowalecki.dietplannerrestapi.restController.meal;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplannerrestapi.exception.GenerateMealBoardException;
import pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException.MealsNotFoundException;
import pl.kowalecki.dietplannerrestapi.model.DTO.FoodBoardPageDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.MealHistoryResponse;
import pl.kowalecki.dietplannerrestapi.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.*;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;
import pl.kowalecki.dietplannerrestapi.model.projection.MealProjection;
import pl.kowalecki.dietplannerrestapi.services.meal.history.IMealHistoryService;
import pl.kowalecki.dietplannerrestapi.services.meal.IMealService;
import pl.kowalecki.dietplannerrestapi.services.meal.MealServiceImpl;
import pl.kowalecki.dietplannerrestapi.services.mealView.IMealViewService;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping(value = "/meal")
@RestController
@AllArgsConstructor
@Slf4j
public class MealController {

    private final IMealService mealService;
    private final IMealViewService mealViewService;
    private final IMealHistoryService mealHistoryService;
    private final MealServiceImpl mealServiceImpl;

    @GetMapping("/getMealsToBoard")
    public ResponseEntity<List<MealView>> getAllUserMeals(@RequestHeader("X-User-Id") String userId) {
        List<MealView> mealList = mealViewService.getMealsToBoard(Long.valueOf(userId));
        return ResponseEntity.status(HttpStatus.OK).body(mealList);
    }

    @GetMapping("/getMeals")
    public ResponseEntity<Page<MealProjection>> getMeals(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "all") String mealType) {
        return ResponseEntity.ok(mealService.getMeals(userId, page, size, mealType));
    }

//    //FIXME normalnie usuwamy :v;
//    @GetMapping(value = "/deleteMeal/{id}")
//    public ResponseEntity<?> deleteMealById(@PathVariable Long id) {
//        mealService.deleteMealById(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PostMapping("/addOrUpdateMeal")
    public ResponseEntity<Void> addMeal(@RequestHeader("X-User-Id") String userId, @RequestBody @Valid AddMealRequestDTO meal) {
        try {
            mealService.addOrUpdateMeal(Long.valueOf(userId), meal);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            log.error("Entity not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("AddMeal: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/getShoppingListData")
    public ResponseEntity<List<IngredientsToBuy> > generateShoppingList(@RequestHeader("X-User-Id") String userId, @RequestBody String pageId){
        MealHistoryProjection mealHistory = mealHistoryService.findMealHistoryByUUID(UUID.fromString(pageId), Long.valueOf(userId));
        List<Long> mealIds = Arrays.stream(mealHistory.getMealsIds().split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        List<IngredientsToBuy> ingredientToBuyDTOList =
                mealService.getMealIngredientsFinalList(mealIds, mealHistory.getMultiplier());

        return ResponseEntity.ok(ingredientToBuyDTOList);
    }

    @PostMapping("/generateFoodBoard")
    public ResponseEntity<String> generateFoodBoard(@RequestHeader("X-User-Id") String userId, @RequestBody FoodBoardPageDTO foodBoardPageDTO, HttpServletRequest request) {
        try {
            MealHistory mealHistory = MealHistory.builder()
                    .userId(Long.valueOf(userId))
                    .public_id(UUID.randomUUID())
                    .mealsIds(foodBoardPageDTO.getMealIds().stream().map(String::valueOf).collect(Collectors.joining(",")))
                    .created(LocalDateTime.now())
                    .multiplier(foodBoardPageDTO.getMultiplier())
                    .build();
            mealHistoryService.saveMealHistory(mealHistory);

            return ResponseEntity.ok(mealHistory.getPublic_id().toString());
        } catch (Exception e) {
            log.error("generateFoodBoard: {}", e.getMessage());
            throw new GenerateMealBoardException("generateFoodBoard: " + e.getMessage());
        }
    }

    @GetMapping(value = "/getMealStarterPack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MealStarterPackDTO> getMealStarterPack() {
        MealStarterPackDTO mealStarterPackDTO = mealService.buildStarterPack();
        return ResponseEntity.ok().body(mealStarterPackDTO);

    }

    @PostMapping(value = "/getMealNamesById")
    public ResponseEntity<List<String>> getMealNamesById(@RequestHeader("X-User-Id") String userId, @RequestBody String pageId) {
        try {
            List<String> mealNames = mealService.getMealNamesByHistoryAndUserId(pageId, Long.valueOf(userId));
            return ResponseEntity.ok(mealNames);
        } catch (Exception e) {
            log.error("getMealNamesById: {}", e.getMessage());
            throw new MealsNotFoundException("mealsNotFound");
        }
    }

    @GetMapping(value = "/getMealHistory")
    public ResponseEntity<List<MealHistoryProjection>> getMealHistory(@RequestHeader("X-User-Id") String userId) {
        List<MealHistoryProjection> mealHistoryList = mealHistoryService.findMealHistoriesByUserId(Long.valueOf(userId));
        return ResponseEntity.ok(mealHistoryList);
    }

    @PostMapping(value = "/getMealHistoryById")
    public ResponseEntity<MealHistoryResponse> getMealHistory(@RequestHeader("X-User-Id") String userId, @RequestBody String id) {
        MealHistoryProjection mealHistory = mealHistoryService.findMealHistoryByUUID(UUID.fromString(id), Long.valueOf(userId));
        String ids = mealHistory.getMealsIds();
        List<Long> mealIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<IngredientsToBuy> ingredientToBuyDTOList = mealServiceImpl.getMealIngredientsFinalList(mealIds, mealHistory.getMultiplier());

        MealHistoryResponse mealHistoryResponse = MealHistoryResponse.builder()
                .multiplier(mealHistory.getMultiplier())
                .mealNames(mealServiceImpl.getMealNamesByIdList(mealIds))
                .ingredientsToBuy(ingredientToBuyDTOList)
                .documentId(mealHistory.getPublic_id().toString())
                .build();
        return ResponseEntity.ok(mealHistoryResponse);
    }

    @GetMapping(value = "/getMealDetails/{id}")
    public ResponseEntity<MealDTO> getMealDetails(@RequestHeader("X-User-Id") String userId,
                                                              @PathVariable Long id){
            return ResponseEntity.ok(mealService.getMealDetailsByMealId(id, Long.valueOf(userId)));
    }

    @GetMapping("/searchMealsByName")
    public ResponseEntity<List<MealProjection>> searchIngredients(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam("query") String query) {

        return ResponseEntity.ok(mealService.findMealsByNameAndUserIdOrPublic(Long.valueOf(userId), query));
    }

}
