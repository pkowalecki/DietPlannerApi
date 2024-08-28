package pl.kowalecki.dietplannerrestapi.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public enum MealType {

    BREAKFAST("śniadanie", "breakfast"),
    SNACK("przekąska", "snack"),
    LUNCH("obiad", "lunch"),
    SUPPER("kolacja", "supper")
    ;

    private String mealTypePl;
    private String mealTypenEn;


    public static MealType getByShortName(String shortName) {
        for (MealType ingredientUnit : values()) {
            if (ingredientUnit.getMealTypePl().equals(shortName)) return ingredientUnit;
        }
        return null;
    }

}
