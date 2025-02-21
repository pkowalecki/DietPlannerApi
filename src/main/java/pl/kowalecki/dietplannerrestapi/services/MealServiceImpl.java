package pl.kowalecki.dietplannerrestapi.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.IngredientsListHelper;
import pl.kowalecki.dietplannerrestapi.exception.MealsNotFoundException;
import pl.kowalecki.dietplannerrestapi.mapper.IngredientNameMapper;
import pl.kowalecki.dietplannerrestapi.mapper.MealMapper;
import pl.kowalecki.dietplannerrestapi.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.*;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplannerrestapi.model.projection.MealProjection;
import pl.kowalecki.dietplannerrestapi.repository.IngredientNamesRepository;
import pl.kowalecki.dietplannerrestapi.repository.MealRepository;
import pl.kowalecki.dietplannerrestapi.repository.MealViewRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MealServiceImpl implements IMealService {

    private final MealRepository mealRepository;
    private final MealViewRepository mealViewRepository;
    private final IngredientNamesRepository ingredientNamesRepository;
    private final IngredientNameMapper ingredientNameMapper;
    private final MealMapper mealMapper;
    private final IMealHistoryService mealHistoryService;

    @Override
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    @Override
    public List<MealView> getAllMealsByUserId(Long userId) {
        List<MealView> mealNames = mealViewRepository.findAllByUserId(userId);
        return mealNames;
    }

    @Override
    public Meal getMealById(Long id) {
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found!"));
    }

    @Override
    public Page<MealProjection> findAllByUserIdAndMealType(Long userId, String mealType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            if(mealType.equals("private")){
                return mealRepository.findAllByUserId(userId, pageable);
            }
            MealType type = MealType.valueOf(mealType.toUpperCase());
            return mealRepository.findAllByUserIdAndMealTypes(userId, type, pageable);
        } catch (IllegalArgumentException e) {
            return Page.empty(pageable);
        }
    }


    @Override
    public Page<MealProjection> findAllByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mealRepository.findAllByUserId(userId, pageable);
    }


    @Override
    @Transactional
    public void deleteMealById(Long id) {
        if (!mealRepository.existsById(id)) {
            throw new EntityNotFoundException("Meal not found!");
        }
        mealRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addOrUpdateMeal(Long userId, AddMealRequestDTO mealRequest) {

        Long mealId = mealRequest.getMealId();
        Meal meal;
        if (mealId!=null && mealId!=-1){
            meal = mealRepository.findMealByMealIdAndUserId(mealId, userId)
                    .orElseThrow(() -> new EntityNotFoundException("Meal not found or access denied"));
            meal.setEditDate(LocalDateTime.now());
        }else{
            meal = new Meal();
            meal.setUserId(userId);
        }

        updateMealData(meal, mealRequest);

        mealRepository.save(meal);
    }

    private void updateMealData(Meal meal, AddMealRequestDTO mealRequest) {
        meal.setName(mealRequest.getMealName());
        meal.setDescription(mealRequest.getDescription());
        meal.setRecipe(mealRequest.getRecipe());
        meal.setNotes(mealRequest.getNotes());
        meal.setPortions(mealRequest.getPortions());


        if (meal.getIngredients() !=null && !meal.getIngredients().isEmpty())meal.getIngredients().clear();
        if (mealRequest.getIngredients() != null && !mealRequest.getIngredients().isEmpty()) {
            List<Ingredient> ingredients = buildIngredients(mealRequest.getIngredients(), meal);
            meal.setIngredients(ingredients);
        }

        if (meal.getMealTypes()!=null && !meal.getMealTypes().isEmpty()) meal.getMealTypes().clear();
        if (mealRequest.getMealTypes() != null && !mealRequest.getMealTypes().isEmpty()) {
            List<MealType> mealTypes = mapMealTypes(mealRequest.getMealTypes());
            meal.setMealTypes(mealTypes);
        }
    }

    @Override
    public MealStarterPackDTO buildStarterPack() {
        List<IngredientNameDTO> ingredientNameList = getMealIngredientNames();
        List<MealTypeDTO> mealTypeList = MealTypeDTO.buildMealTypeList();
        List<IngredientUnitDTO> ingredientUnitList = IngredientUnitDTO.buildIngredientUnitDTO();
        List<MeasurementTypeDTO> measurementTypeList = MeasurementTypeDTO.buildMeasurementTypeDTO();
        return MealStarterPackDTO.builder()
                .measurementTypeList(measurementTypeList)
                .ingredientUnitList(ingredientUnitList)
                .ingredientNameList(ingredientNameList)
                .mealTypeList(mealTypeList)
                .build();
    }


    public List<Ingredient> getMealIngredientsByMealId(Long mealId) {
        Meal meal = mealRepository.findById(mealId).orElse(null);
        if (meal == null) {
            return Collections.emptyList();
        }
        return meal.getIngredients();
    }

    public Map<Boolean, List<Ingredient>> getMealTypeAndIngredientsByMealId(Long mealId) {
        Map<Boolean, List<Ingredient>> map = new HashMap<>();
        Meal meal = mealRepository.findById(mealId).orElse(null);
        if (meal == null) {
            return map;
        }
        map.put(meal.getMealTypes().stream()
                .anyMatch(type -> "SNACK".equals(type.getMealTypeEn())), meal.getIngredients());
        return map;
    }

    @Override
    public List<IngredientsToBuy> getMealIngredientsFinalList(List<Long> ids, Double multiplier) {
        IngredientsListHelper helper = new IngredientsListHelper();
        List<Ingredient> combinedIngredients = new ArrayList<>();

        for (Long id : ids) {
            if (id == 0) continue;
            List<Ingredient> ingredients = getMealIngredientsByMealId(id);
            combinedIngredients.addAll(ingredients);
        }
        return helper.generateShoppingList(combinedIngredients, multiplier);
    }

    @Override
    public List<Meal> findMealsByMealIdIn(List<Long> mealIds) {
        return mealRepository.findMealsByMealIdIn(mealIds);
    }

    @Override
    public List<String> getMealNamesByIdList(List<Long> mealIds) {
        List<String> mealNames = new ArrayList<>();
        for (Long mealId : mealIds) {
            if (mealId == null || mealId == 0 || mealId == -1) {
                mealNames.add("-");
                continue;
            }
            mealNames.add(mealRepository.getMealNameByMealId(mealId));
        }
        return mealNames;
    }

    public List<IngredientNameDTO> getMealIngredientNames() {
        return ingredientNamesRepository.findAll().stream()
                .map(ingredientNameMapper::ingredientNameDTO)
                .collect(Collectors.toList());
    }


    private List<MealType> mapMealTypes(List<Integer> mealTypes) {
        if (mealTypes == null || mealTypes.isEmpty()) {
            throw new IllegalArgumentException("Meal types cannot be empty!");
        }
        return mealTypes.stream()
                .map(MealType::getMealTypeById)
                .collect(Collectors.toList());
    }

    private List<Ingredient> buildIngredients(List<IngredientDTO> ingredients, Meal meal) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("Ingredients cannot be empty!");
        }
        return ingredients.stream()
                .map(dto -> buildIngredient(dto, meal))
                .collect(Collectors.toList());
    }

    private Ingredient buildIngredient(IngredientDTO ingredientDTO, Meal meal) {
        IngredientName ingredientName = ingredientNamesRepository.findById(ingredientDTO.getIngredientNameId())
                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + ingredientDTO.getIngredientNameId()));

        return Ingredient.builder()
                .ingredientAmount(ingredientDTO.getIngredientAmount())
                .ingredientUnit(IngredientUnit.getById(Integer.parseInt(ingredientDTO.getIngredientUnit())))
                .measurementValue(ingredientDTO.getMeasurementValue())
                .measurementType(MeasurementType.getById(Integer.parseInt(ingredientDTO.getMeasurementType())))
                .ingredientNameId(ingredientName)
                .meal(meal)
                .build();
    }

    private Meal buildMeal(Long userId, AddMealRequestDTO mealRequest) {
        return Meal.builder()
                .name(mealRequest.getMealName())
                .description(mealRequest.getDescription())
                .recipe(mealRequest.getRecipe())
                .notes(mealRequest.getNotes())
                .userId(userId)
                .portions(mealRequest.getPortions())
                .build();
    }

    @Override
    public MealDTO getMealDetailsByMealAndUserId(Long id, Long userId) {
        Meal meal = mealRepository.getMealByIdAndUserId(id, userId).orElseThrow(()-> new MealsNotFoundException("Meal not found!"));
        return mealMapper.mealToMealDTO(meal);
    }

    @Override
    public Page<MealProjection> findAllByNameAndUserId(String name, Long userId) {
        Pageable pageable = PageRequest.of(0, 10);
        return mealRepository.findMealByNameContainingIgnoreCaseAndUserId(name, userId, pageable);

    }

    @Override
    public List<String> getMealNamesByHistoryAndUserId(String pageId, Long userId) {
        MealHistoryProjection mealHistoryDTO = mealHistoryService.findMealHistoryByUUID(UUID.fromString(pageId), userId);
        if (mealHistoryDTO != null){
            List<Long> mealIds = Arrays.stream(mealHistoryDTO.getMealsIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            return getMealNamesByIdList(mealIds);
        }
        return Collections.emptyList();
    }
}
