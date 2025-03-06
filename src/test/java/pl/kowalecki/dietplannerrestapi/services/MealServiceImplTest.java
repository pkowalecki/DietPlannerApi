package pl.kowalecki.dietplannerrestapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import pl.kowalecki.dietplannerrestapi.model.projection.MealProjection;
import pl.kowalecki.dietplannerrestapi.repository.MealRepository;
import pl.kowalecki.dietplannerrestapi.services.meal.MealServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MealServiceImplTest {

    @InjectMocks
    private MealServiceImpl mealService;

    @Mock
    private MealRepository mealRepository;

    @Test
    void shouldReturnMealsByName() {
        String query = "Pizza";
        int page = 0;
        int size = 10;

        List<MealProjection> mealProjections = List.of(mock(MealProjection.class));
        Pageable pageable = PageRequest.of(page, size);
        Page<MealProjection> pageResult = new PageImpl<>(mealProjections, pageable, mealProjections.size());

        when(mealRepository.findMealByNameContainingIgnoreCase(query, pageable))
                .thenReturn(pageResult);

        Page<MealProjection> result = mealService.findAllByName(query);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }
}