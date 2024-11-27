package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import lombok.*;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MealTypeDTO {
    private int id;
    private String mealTypePl;
    private String mealTypeEn;


    public static List<MealTypeDTO> buildMealTypeList() {
        return Arrays.stream(MealType.values()).map(
                        mealType -> MealTypeDTO.builder()
                                .id(mealType.getId())
                                .mealTypePl(mealType.getMealTypePl())
                                .mealTypeEn(mealType.getMealTypeEn()).build())
                .collect(Collectors.toList());
    }
}