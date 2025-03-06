package pl.kowalecki.dietplannerrestapi.model.DTO.meal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Entity(name = "meal_mealtypes_view")
@Immutable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MealView {
    @Id
    private Long mealId;
    private String name;
    private String mealTypes;
    private Long userId;
    private boolean mealPublic;
}