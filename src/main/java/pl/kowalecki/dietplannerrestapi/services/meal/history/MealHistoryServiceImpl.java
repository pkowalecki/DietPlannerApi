package pl.kowalecki.dietplannerrestapi.services.meal.history;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.model.DTO.meal.MealHistoryProjection;
import pl.kowalecki.dietplannerrestapi.model.MealHistory;
import pl.kowalecki.dietplannerrestapi.repository.MealHistoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MealHistoryServiceImpl implements IMealHistoryService {

    @Autowired
    private MealHistoryRepository mealHistoryRepository;


    @Override
    public void saveMealHistory(MealHistory mealHistory) {
        mealHistoryRepository.save(mealHistory);
    }

    @Override
    public List<MealHistoryProjection> findMealHistoriesByUserId(Long userId) {
        return  mealHistoryRepository.findAllByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Meal history not found!")
        );
    }

    @Override
    public MealHistoryProjection findMealHistoryByUUID(UUID id, Long userId) {
        return mealHistoryRepository.findMealHistoryByPublicIdAndUserId(id, userId).orElseThrow(
                () -> new EntityNotFoundException("Meal history not found!")
        );
    }

}
