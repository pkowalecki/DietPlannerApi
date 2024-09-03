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
    private String mealTypeEn;


    public static MealType getByShortName(String shortName) {
        for (MealType mealType : values()) {
            if (mealType.getMealTypePl().equals(shortName)) return mealType;
        }
        return null;
    }

}
