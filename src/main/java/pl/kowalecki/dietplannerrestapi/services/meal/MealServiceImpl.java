package pl.kowalecki.dietplannerrestapi.services.meal;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.IngredientsListHelper;
import pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException.MealsNotFoundException;
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
import pl.kowalecki.dietplannerrestapi.services.meal.history.IMealHistoryService;

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
    public void addOrUpdateMeal(Long userId, AddMealRequestDTO mealRequest) {

        Long mealId = mealRequest.getMealId();
        Meal meal;
        if (mealId != null && mealId != -1) {
            meal = mealRepository.findMealByMealIdAndUserId(mealId, userId)
                    .orElseThrow(() -> new EntityNotFoundException("Meal not found or access denied"));
            meal.setEditDate(LocalDateTime.now());
        } else {
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
        updateIngredients(meal, mealRequest.getIngredients());
        updateMealTypes(meal, mealRequest.getMealTypes());
        meal.setMealPublic(mealRequest.isMealPublic());
    }

    private void updateMealTypes(Meal meal, List<Integer> mealTypesRequest) {
        if (meal.getMealId() == null || meal.getMealId() == -1) {
            meal.setMealTypes(mapMealTypes(mealTypesRequest));
        } else {
            meal.getMealTypes().clear();
            meal.setMealTypes(mapMealTypes(mealTypesRequest));
        }
    }

    private void updateIngredients(Meal meal, List<IngredientDTO> ingredientsRequest) {
        if (meal.getIngredients() == null) {
            meal.setIngredients(new ArrayList<>());
        }
        Map<Long, Ingredient> existingIngredients = meal.getIngredients().stream()
                .collect(Collectors.toMap(ing -> ing.getIngredientNameId().getId(), ing -> ing));

        List<Ingredient> updatedIngredients = new ArrayList<>();
        if (ingredientsRequest != null && !ingredientsRequest.isEmpty()) {
            for (IngredientDTO dto : ingredientsRequest) {
                IngredientName ingredientName = ingredientNamesRepository.findById(dto.getIngredientNameId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + dto.getIngredientNameId()));

                if (existingIngredients.containsKey(ingredientName.getId())) {
                    Ingredient existingIngredient = existingIngredients.get(ingredientName.getId());

                    if (!existingIngredient.equals(buildIngredient(dto, meal))) {

                        existingIngredient.setIngredientAmount(dto.getIngredientAmount());
                        existingIngredient.setIngredientUnit(IngredientUnit.getById(Integer.parseInt(dto.getIngredientUnit())));
                        existingIngredient.setMeasurementValue(dto.getMeasurementValue());
                        existingIngredient.setMeasurementType(MeasurementType.getById(Integer.parseInt(dto.getMeasurementType())));
                    }
                    updatedIngredients.add(existingIngredient);
                } else {
                    updatedIngredients.add(buildIngredient(dto, meal));
                }
            }
        }
        meal.getIngredients().clear();
        meal.getIngredients().addAll(updatedIngredients);
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
    public MealDTO getMealDetailsByMealId(Long id, Long userId) {
        Meal meal = mealRepository.getMealById(id)
                .orElseThrow(() -> new MealsNotFoundException("Nie znaleziono przepisu."));

        if (!meal.isMealPublic()){
            if(!meal.getUserId().equals(userId)){
                throw new AccessDeniedException("Nie masz uprawnień do żądanego zasobu");
            }
        }

        boolean canEdit = meal.getUserId().equals(userId);

        return mealMapper.mealToMealDTO(meal, canEdit);
    }

    @Override
    public Page<MealProjection> findAllByName(String name) {
        Pageable pageable = PageRequest.of(0, 10);
        return mealRepository.findMealByNameContainingIgnoreCase(name, pageable);

    }

    @Override
    public List<String> getMealNamesByHistoryAndUserId(String pageId, Long userId) {
        MealHistoryProjection mealHistoryDTO = mealHistoryService.findMealHistoryByUUID(UUID.fromString(pageId), userId);
        if (mealHistoryDTO != null) {
            List<Long> mealIds = Arrays.stream(mealHistoryDTO.getMealsIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            return getMealNamesByIdList(mealIds);
        }
        return Collections.emptyList();
    }

    @Override
    public Page<MealProjection> findAllByPublic(boolean isPublic, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mealRepository.findAllByMealPublic(true, pageable);
    }


    @Override
    public Page<MealProjection> getMeals(String userId, int page, int size, String mealType) {
        Page<MealProjection> meals;
        if ("all".equalsIgnoreCase(mealType)) {
            meals = findAllByUserIdOrPublic(Long.valueOf(userId), page, size);
        } else if ("private".equalsIgnoreCase(mealType)) {
            meals = findAllByUserId(Long.valueOf(userId), page, size);
        } else {
            meals = findAllByUserIdAndMealType(Long.valueOf(userId), mealType, page, size);
        }
        return meals;
    }

    private Page<MealProjection> findAllByUserIdOrPublic(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mealRepository.findAllByUserIdOrMealPublic(userId, true, pageable);
    }

    private Page<MealProjection> findAllByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mealRepository.findAllByUserId(userId, pageable);
    }

    private Page<MealProjection> findAllByUserIdAndMealType(Long userId, String mealType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        MealType type = MealType.valueOf(mealType.toUpperCase());
        return mealRepository.findAllByUserIdAndMealTypes(userId, type, pageable);
    }

    @Override
    public List<MealProjection> findMealsByNameAndUserIdOrPublic(Long userId, String query) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MealProjection> meals = mealRepository.findAllByNameContainingIgnoreCaseAndUserIdOrNameContainingIgnoreCaseAndMealPublic(query, userId, query, true, pageable);
        return meals.getContent();
    }
}
