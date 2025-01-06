package pl.kowalecki.dietplannerrestapi.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.IngredientsListHelper;
import pl.kowalecki.dietplannerrestapi.mapper.IngredientNameMapper;
import pl.kowalecki.dietplannerrestapi.model.DTO.MealStarterPackDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.*;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientsToBuy;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplannerrestapi.repository.IngredientNamesRepository;
import pl.kowalecki.dietplannerrestapi.repository.MealRepository;
import pl.kowalecki.dietplannerrestapi.repository.MealViewRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MealServiceImpl implements IMealService {

    private final MealRepository mealRepository;
    private final MealViewRepository mealViewRepository;
    private final IngredientNamesRepository ingredientNamesRepository;
    private final IngredientNameMapper ingredientNameMapper;

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
    @Transactional
    public void deleteMealById(Long id) {
        if (!mealRepository.existsById(id)) {
            throw new EntityNotFoundException("Meal not found!");
        }
        mealRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addMeal(Long userId, AddMealRequestDTO mealRequest) {
        Meal meal = buildMeal(userId, mealRequest);
        List<Ingredient> ingredients = buildIngredients(mealRequest.getIngredients(), meal);
        List<MealType> mealTypes = mapMealTypes(mealRequest.getMealTypes());
        meal.setIngredients(ingredients);
        meal.setMealTypes(mealTypes);
        mealRepository.save(meal);
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

    public List<String> getMealNamesByIdList(List<Long> list) {
        List<String> mealNames = new ArrayList<>();
        for (Long mealId : list) {
            if (mealId == 0) {
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
            return Collections.emptyList();
        }
        return mealTypes.stream()
                .map(MealType::getMealTypeById)
                .collect(Collectors.toList());
    }

    private List<Ingredient> buildIngredients(List<IngredientDTO> ingredients, Meal meal) {
        if (ingredients == null || ingredients.isEmpty()) {
            return Collections.emptyList();
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

}
