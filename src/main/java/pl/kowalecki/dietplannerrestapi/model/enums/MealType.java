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

    BREAKFAST(1,"śniadanie", "breakfast"),
    SNACK(2,"przekąska", "snack"),
    LUNCH(3,"obiad", "lunch"),
    SUPPER(4,"kolacja", "supper"),
    OTHER(0,"inne", "other")
    ;

    private int id;
    private String mealTypePl;
    private String mealTypeEn;


    public static MealType getById(int id) {
        for (MealType mealType : values()) {
            if (mealType.id == id) {
                return mealType;
            }
        }
        throw new IllegalArgumentException("No enum constant for id: " + id);
    }

    public static MealType getFromMealTypePl(String mealTypePl) {
        for (MealType mealType : values()) {
            if (mealType.mealTypePl.equalsIgnoreCase(mealTypePl)) {
                return mealType;
            }
        }
        throw new IllegalArgumentException("No enum constant for mealTypePl: " + mealTypePl);
    }

}
