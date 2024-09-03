package pl.kowalecki.dietplannerrestapi.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.IngredientsListHelper;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.AddMealRequestDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.IngredientToBuyDTO;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.User;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;
import pl.kowalecki.dietplannerrestapi.model.ingredient.IngredientName;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;
import pl.kowalecki.dietplannerrestapi.repository.IngredientNamesRepository;
import pl.kowalecki.dietplannerrestapi.repository.IngredientRepository;
import pl.kowalecki.dietplannerrestapi.repository.MealRepository;
import pl.kowalecki.dietplannerrestapi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MealServiceImpl implements MealService{

    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final UserServiceImpl userService;
    private final IngredientNamesRepository ingredientNamesRepository;
    private final UserRepository userRepository;

    @Override
    public List<Meal> getAllMeals(){
        return mealRepository.findAll();
    }

    @Override
    public Meal getMealById(Long id){
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + id));
    }

    @Override
    public boolean deleteMealById(Long id){
//        try {
//            mealRepository.deleteById(id);
//            return true;
//        }catch (HibernateError error){
//            error.printStackTrace();
//            return false;
//        }
        return true;
    }

    @Override
    @Transactional
    public void addMeal(Integer userId, AddMealRequestDTO mealRequest) throws Exception{
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
            Meal meal = new Meal();
            if (mealRequest.getIngredients() == null) {
                meal.setIngredients(new ArrayList<>());
            }
            meal.setName(mealRequest.getMealName());
            meal.setDescription(mealRequest.getDescription());
            meal.setRecipe(mealRequest.getRecipe());
            meal.setNotes(mealRequest.getNotes());
            meal.setAdditionDate(LocalDateTime.now());
            meal.setDeleted(false);
            mealRepository.save(meal);

            List<MealType> mealTypes = mealRequest.getMealTypes().stream()
                    .map(MealType::getByShortName)
                    .collect(Collectors.toList());
            meal.setMealTypes(mealTypes);

            List<Ingredient> ingredients = mealRequest.getIngredients().stream()
                    .map(ingredientDTO -> {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setIngredientAmount(ingredientDTO.getIngredientAmount());
                        ingredient.setIngredientUnit(IngredientUnit.getByShortName(ingredientDTO.getIngredientUnit()));
                        ingredient.setMeasurementValue(ingredientDTO.getMeasurementValue());
                        ingredient.setMeasurementType(MeasurementType.getMeasurementTypeByName(ingredientDTO.getMeasurementType()));

                        IngredientName ingredientName = ingredientNamesRepository.findById(ingredientDTO.getIngredientNameId())
                                .orElseThrow(() -> new EntityNotFoundException("Ingredient not found with id: " + ingredientDTO.getIngredientNameId()));
                        ingredient.setIngredientNameId(ingredientName);
                        ingredient.setMeal(meal);
                        return ingredient;
                    }).collect(Collectors.toList());
            meal.setIngredients(ingredients);

            user.getMealList().add(meal);

            mealRepository.save(meal);
            userRepository.save(user);
    }

    public List<Ingredient> getMealIngredientsByMealId(Long mealId){
        Meal meal = mealRepository.findById(mealId).orElse(null);
        if (meal == null) {
            return Collections.emptyList();
        }
        return meal.getIngredients();
    }

    public Map<Boolean, List<Ingredient>> getMealTypeAndIngredientsByMealId(Long mealId){
        Map<Boolean, List<Ingredient>> map = new HashMap<>();
        Meal meal = mealRepository.findById(mealId).orElse(null);
        if (meal == null) {
            return map;
        }
        map.put(meal.getMealTypes().stream()
                .anyMatch(type -> "SNACK".equals(type.getMealTypeEn())), meal.getIngredients());
        return map;
    }

    public List<IngredientToBuyDTO> getMealIngredientsFinalList(List<Long> ids, Double multiplier) {
        List<Ingredient> combinedIngredients = new ArrayList<>();

        for (Long id : ids) {
            if (id == 0) continue;
            List<Ingredient> ingredients = getMealIngredientsByMealId(id);
            combinedIngredients.addAll(ingredients);
        }
        List<Ingredient> ingredients = IngredientsListHelper.prepareIngredientsList(combinedIngredients, multiplier);

        List<IngredientToBuyDTO> ingredientsToBuy = new ArrayList<>();

        for (Ingredient ingredient : ingredients){
            IngredientToBuyDTO ingredientDTO = new IngredientToBuyDTO(ingredient.getIngredientNameId().getName(), ingredient.getIngredientAmount().toString(), ingredient.getIngredientUnit().getShortName(), ingredient.getMeasurementValue().toString(), ingredient.getMeasurementType().getMeasurementName().toString());
            ingredientsToBuy.add(ingredientDTO);
        }


        return ingredientsToBuy;
    }
    public Map<IngredientUnit, List<String>> getIngredientUnitMap(){
        Map<IngredientUnit, List<String>> ingredientListMap = IngredientUnit.getIngredientUnitMap();
        return ingredientListMap;
    }
    public  Map<MeasurementType, List<String>> getMeasurementTypeMap(){
        Map<MeasurementType, List<String>> measurementNames = MeasurementType.getMeasurementTypeMap();
        return measurementNames;
    }

    @Override
    public List<Meal> getMealByUserId(Long userId) {
        return mealRepository.findMealsByUserId(userId);
    }


    public List<String> getMealNamesByIdList(List<Long> list) {
        List<String> mealNames = new ArrayList<>();
        for (Long mealId : list){
            if (mealId == 0){
                mealNames.add("-");
                continue;
            }
            mealNames.add(mealRepository.getMealNameByMealId(mealId));
        }
        return mealNames;
    }

    public List<IngredientName> getMealIngredientNames() {
        return ingredientNamesRepository.findAll();
    }
}
