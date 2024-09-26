package pl.kowalecki.dietplannerrestapi.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealHistoryDTO;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;
import pl.kowalecki.dietplannerrestapi.repository.MealHistoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MealHistoryServiceImpl implements MealHistoryService {

    @Autowired
    private MealHistoryRepository mealHistoryRepository;


    @Override
    public void saveMealHistory(MealHistory mealHistory) {
        mealHistoryRepository.save(mealHistory);
    }

    @Override
    public List<MealHistoryDTO> findMealHistoriesByUserId(Long userId) {
        List<MealHistory> mealHistory = mealHistoryRepository.findAllByUserId(userId);
        if (mealHistory.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<MealHistoryDTO> mealHistoryDTO = new ArrayList<>();
            for (MealHistory historicalMeal : mealHistory) {
                mealHistoryDTO.add(
                        MealHistoryDTO.builder()
                                .id(historicalMeal.getId())
                                .userId(historicalMeal.getUserId())
                                .mealsIds(historicalMeal.getMealsIds())
                                .created(historicalMeal.getCreated())
                                .build());
            }
            return mealHistoryDTO;
        }
    }
}
