package pl.kowalecki.dietplannerrestapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meals")
public class Meal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;
    private LocalDateTime additionDate;
    private LocalDateTime editDate;
    private String name;
    @Column(columnDefinition="TEXT")
    private String description;
    @Column(columnDefinition="TEXT")
    private String recipe;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Ingredient> ingredients;
    @Column(columnDefinition="TEXT")
    private String notes;
    @Enumerated(EnumType.STRING)
    private List<MealType> mealTypes;

    private boolean isDeleted;


    @Override
    public String toString() {
        return "Meal{" +
                "mealId=" + mealId +
                ", additionDate=" + additionDate +
                ", editDate=" + editDate +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", recipe='" + recipe + '\'' +
                ", ingredients=" + ingredients +
                ", notes='" + notes + '\'' +
                ", mealTypes=" + mealTypes +
                ", isDeleted=" + isDeleted +
                '}';
    }

}
