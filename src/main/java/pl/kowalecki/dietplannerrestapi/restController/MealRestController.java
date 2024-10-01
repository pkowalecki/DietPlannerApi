package pl.kowalecki.dietplannerrestapi.restController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.kowalecki.dietplannerrestapi.model.DTO.FoodBoardPageDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.MealHistoryResponse;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.*;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;
import pl.kowalecki.dietplannerrestapi.model.User;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplannerrestapi.repository.MealRepository;
import pl.kowalecki.dietplannerrestapi.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplannerrestapi.services.IApiService;
import pl.kowalecki.dietplannerrestapi.services.MealHistoryService;
import pl.kowalecki.dietplannerrestapi.services.MealServiceImpl;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsImpl;

import java.time.LocalDateTime;
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
    private final MealRepository mealRepository;
    private final IApiService apiService;
    private final MealHistoryService mealHistoryService;

    @GetMapping("/allMeal")
    public ResponseEntity<ResponseBodyDTO> getListMeal() {
        List<Meal> mealList = mealServiceImpl.getAllMeals();
        if (!mealList.isEmpty()) {
            Map<String, List<Meal>> respoData = new HashMap<>();
            respoData.put("mealList", mealList);
            ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
                    .status(ResponseBodyDTO.ResponseStatus.OK)
                    .data(respoData)
                    .build();
            return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseBodyDTO.builder()
                .status(ResponseBodyDTO.ResponseStatus.ERROR)
                .message("Meals not found")
                .build(), HttpStatus.OK);
    }

    @GetMapping(value = "/getMeal/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        if (mealServiceImpl.getMealById(id) != null)
            return new ResponseEntity<>(mealServiceImpl.getMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/deleteMeal/{id}")
    public ResponseEntity<?> deleteMealById(@PathVariable Long id) {
        if (mealServiceImpl.getMealById(id) != null)
            return new ResponseEntity<>(mealServiceImpl.deleteMealById(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/addMeal")
    public ResponseEntity<ResponseBodyDTO> addMeal(@RequestBody AddMealRequestDTO newMeal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl userDetails) {
                try {
                    mealServiceImpl.addMeal(userDetails.getId(), newMeal);
                } catch (Exception e) {
                    ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
                            .status(ResponseBodyDTO.ResponseStatus.ERROR)
                            .message("Meal not created")
                            .build();
                    log.error("AddMeal: {}", e.getMessage());
                    e.printStackTrace();
                    return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
                }
            }
        }

        ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
                .status(ResponseBodyDTO.ResponseStatus.OK)
                .message("Meal created")
                .build();
        return new ResponseEntity<>(responseBodyDTO, HttpStatus.CREATED);
    }

    @PostMapping("/generateFoodBoard")
    public ResponseEntity<ResponseBodyDTO> generateFoodBoard(@RequestBody FoodBoardPageDTO foodBoardPageDTO, HttpServletRequest request) {
        Map<String, List<?>> responseData = new HashMap<>();
        try {
            List<Meal> mealList = mealRepository.findMealsByMealIdIn(foodBoardPageDTO.getMealIds());
            List<IngredientToBuyDTO> ingredientToBuyDTOList = mealServiceImpl.getMealIngredientsFinalList(foodBoardPageDTO.getMealIds(), foodBoardPageDTO.getMultiplier());
            responseData.put("mealList", mealList);
            responseData.put("ingredientToBuyDTOList", ingredientToBuyDTOList);

        } catch (Exception e) {
            ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
                    .status(ResponseBodyDTO.ResponseStatus.ERROR)
                    .message("Meal not created")
                    .build();
            log.error("generateFoodBoard: {}", e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
        }

        Integer userId = authJwtUtils.getUserIdFromToken(request);
        MealHistory mealHistory = MealHistory.builder()
                .userId(Long.valueOf(userId))
                .public_id(UUID.randomUUID())
                .mealsIds(foodBoardPageDTO.getMealIds().stream().map(String::valueOf).collect(Collectors.joining(",")))
                .created(LocalDateTime.now())
                .multiplier(foodBoardPageDTO.getMultiplier())
                .build();
        mealHistoryService.saveMealHistory(mealHistory);

        ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
                .status(ResponseBodyDTO.ResponseStatus.OK)
                .data(responseData)
                .build();
        return new ResponseEntity<>(responseBodyDTO, HttpStatus.OK);
    }
//    @PostMapping("/generateFoodRecipe")
//    public ResponseEntity<List<FoodDTO>> generateFoodRecipe(@RequestParam("ids") String ids, @RequestParam("multiplier") Double multiplier) {
//        List<Long> idsList = Arrays.stream(ids.split(","))
//                .map(Long::parseLong)
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(mealRepository.getMealRecipeFinalList(idsList, multiplier), HttpStatus.OK);
//    }

    @GetMapping(value = "/getMealIngredientsList/{id}")
    public ResponseEntity<List<Ingredient>> getMealIngredientsByMealId(@PathVariable Long id) {
        if (mealServiceImpl.getMealIngredientsByMealId(id) != null)
            return new ResponseEntity<>(mealServiceImpl.getMealIngredientsByMealId(id), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/getIngredientMap")
    public ResponseEntity<Map<IngredientUnit, List<String>>> getListIngredientUnit() {
        return new ResponseEntity<>(mealServiceImpl.getIngredientUnitMap(), HttpStatus.OK);
    }

    @GetMapping(value = "/getMeasurementMap")
    public ResponseEntity<Map<MeasurementType, List<String>>> getListMeasurementName() {
        return new ResponseEntity<>(mealServiceImpl.getMeasurementTypeMap(), HttpStatus.OK);
    }

    @GetMapping(value = "/getMealsByUserId/{id}")
    public ResponseEntity<List<Meal>> getMealsByUserId(@PathVariable Long id) {
        if (id != null) return new ResponseEntity<>(mealServiceImpl.getMealByUserId(id), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/getMealStarterPack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDTO> getMealStarterPack() {
        try {
            Map<String, List<?>> responseData = new HashMap<>();

            List<IngredientName> ingredientNameList = mealServiceImpl.getMealIngredientNames();
            responseData.put("ingredientsNames", ingredientNameList);
            List<MealType> mealTypeList = Arrays.asList(MealType.values());
            responseData.put("mealTypes", mealTypeList);
            List<IngredientUnit> ingredientUnitList = Arrays.asList(IngredientUnit.values());
            responseData.put("ingredientUnits", ingredientUnitList);
            List<MeasurementType> measurementTypeList = Arrays.asList(MeasurementType.values());
            responseData.put("measurementTypes", measurementTypeList);
            ResponseBodyDTO response = ResponseBodyDTO.builder()
                    .status(ResponseBodyDTO.ResponseStatus.OK)
                    .data(responseData)
                    .build();
            return ResponseEntity.ok()
                    .body(response);

        } catch (Exception e) {
            return getResponseError(e);
        }

    }

    @GetMapping(value = "/ingredients", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDTO> getIngredients() {
        try {
            Map<String, List<IngredientName>> responseData = new HashMap<>();
            List<IngredientName> ingredientNames = mealServiceImpl.getMealIngredientNames();
            responseData.put("ingredients", ingredientNames);
            ResponseBodyDTO response = ResponseBodyDTO.builder()
                    .status(ResponseBodyDTO.ResponseStatus.OK)
                    .data(responseData)
                    .build();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (Exception e) {
            return getResponseError(e);
        }
    }

    private ResponseEntity<ResponseBodyDTO> getResponseError(Exception e) {
        log.error("Error retrieving data", e);
        ResponseBodyDTO errorResponse = ResponseBodyDTO.builder()
                .status(ResponseBodyDTO.ResponseStatus.ERROR)
                .data(Collections.singletonMap("error", "An error occurred"))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    @PostMapping(value = "/getMealNamesById")
    public ResponseEntity<ResponseBodyDTO> getMealNamesById(@RequestBody List<Long> mealIds) {
        Map<String, List<String>> responseData = new HashMap<>();
        try {
            List<String> mealNames = mealServiceImpl.getMealNamesByIdList(mealIds);
            if (!mealNames.isEmpty()) {
                responseData.put("mealNames", mealNames);
                ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
                        .status(ResponseBodyDTO.ResponseStatus.OK)
                        .data(responseData)
                        .build();
                return ResponseEntity.ok().body(responseBodyDTO);
            }
            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK, "No data", responseData, HttpStatus.OK);
        } catch (Exception e) {
            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.ERROR, "No data", responseData, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getMealHistory")
    public ResponseEntity<ResponseBodyDTO> getMealHistory(HttpServletRequest request){
        Map<String, List<MealHistoryDTO>> data = new HashMap<>();
        Integer userId = authJwtUtils.getUserIdFromToken(request);
        List<MealHistoryDTO> mealHistoryList = mealHistoryService.findMealHistoriesByUserId(Long.valueOf(userId));
        data.put("mealHistoryList", mealHistoryList);
        return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK, data, HttpStatus.OK);


    }

    @PostMapping(value = "/getMealHistory")
    public ResponseEntity<ResponseBodyDTO> getMealHistory(@RequestBody String id, HttpServletRequest request, Authentication authentication){
        //sprawdzamy, czy typ się nie podszywa i wysłane ID jest typa od którego przyszedł req
        UserDetails user = (UserDetails) authentication.getPrincipal();
        MealHistory mealHistory = mealHistoryService.findMealHistoryByUUID(UUID.fromString(id));
        if (mealHistory.getUserId().equals(((UserDetailsImpl) user).getId().longValue())){
            Map<String, MealHistoryResponse> data = new HashMap<>();
            MealHistoryResponse response = new MealHistoryResponse();
            /*
            * Wszystko jest git, więc zaczynamy wyciąganie.
            * Mamy IDKI posiłków, więc na tej podstawie tworzymy tabelkę z posiłkami(tak jak w doku) - to nie, tabelkę tworzymy na froncie, plujemy nazwami.
            * Następnie na podstawie ID posiłków tworzymy listę zakupów
            * I chyba tyle
            * */
            String ids = mealHistory.getMealsIds();
            List<Long> mealIds = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            List<String> mealNames = mealServiceImpl.getMealNamesByIdList(mealIds);
            response.setMealNames(mealNames);
            response.setMultiplier(mealHistory.getMultiplier());
            response.setMeals(getMealDTOList(mealIds));
            data.put("mealHistory", response);
            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.OK, data, HttpStatus.OK);
        }else{
            return apiService.returnResponseData(ResponseBodyDTO.ResponseStatus.ERROR, HttpStatus.UNAUTHORIZED);
        }
    }

    //TODO zerknąć na lepszy sposób niż jechanie przepisów w pętli
    private List<MealDTO> getMealDTOList(List<Long> mealIds) {
        List<MealDTO> mealDTOList = new ArrayList<>();
        for (Long mealId : mealIds) {
            if (mealId == 0) continue;
            Meal meal = mealServiceImpl.getMealById(mealId);
            mealDTOList.add(MealDTO.builder()
                            .additionDate(meal.getAdditionDate())
                            .editDate(meal.getEditDate())
                            .name(meal.getName())
                            .description(meal.getDescription())
                            .recipe(meal.getRecipe())
                            .ingredients(setMealDTOIngredients(meal.getIngredients()))
                            .notes(meal.getNotes())
                            .mealTypes(meal.getMealTypes())
                            .isDeleted(meal.isDeleted())
                    .build());
        }
        System.out.println(mealDTOList);
        return mealDTOList;
    }

    private List<MealTypeDTO> setMealDTOTypes(List<MealType> mealTypes) {
        List<MealTypeDTO> mealTypeDTOList = new ArrayList<>();
        for (MealType mealType : mealTypes) {
            mealTypeDTOList.add(MealTypeDTO.builder()
                            .mealTypeEn(mealType.getMealTypeEn())
                            .mealTypePl(mealType.getMealTypePl())
                    .build());
        }
        return mealTypeDTOList;
    }

    private List<IngredientTDTO> setMealDTOIngredients(List<Ingredient> ingredients) {
        List<IngredientTDTO> ingredientDTOList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientDTOList.add(IngredientTDTO.builder()
                            .name(setIngredientName(ingredient.getIngredientNameId()))
                            .ingredientAmount(ingredient.getIngredientAmount())
                            .ingredientUnit(ingredient.getIngredientUnit())
                            .measurementValue(ingredient.getMeasurementValue())
                            .measurementType(ingredient.getMeasurementType())
                    .build());
        }
        return ingredientDTOList;
    }

    private IngredientNameDTO setIngredientName(IngredientName ingredientNameId) {
        return IngredientNameDTO.builder()
                .ingredientName(ingredientNameId.getName())
                .ingredientBrand(ingredientNameId.getBrand())
                .protein(ingredientNameId.getProtein())
                .carbohydrates(ingredientNameId.getCarbohydrates())
                .fat(ingredientNameId.getFat())
                .kcal(ingredientNameId.getKcal())
                .build();
    }

}
