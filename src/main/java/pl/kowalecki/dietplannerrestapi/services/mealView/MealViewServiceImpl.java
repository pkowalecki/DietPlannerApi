package pl.kowalecki.dietplannerrestapi.services.mealView;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealView;
import pl.kowalecki.dietplannerrestapi.repository.MealViewRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MealViewServiceImpl implements IMealViewService {

    private final MealViewRepository mealViewRepository;

    @Override
    public List<MealView> getMealsToBoard(Long userId) {
        return mealViewRepository.findAllByUserIdOrMealPublic(userId, true);
    }
}
