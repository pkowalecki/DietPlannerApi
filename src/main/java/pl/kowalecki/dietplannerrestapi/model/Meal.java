package pl.kowalecki.dietplannerrestapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.kowalecki.dietplannerrestapi.model.enums.MealType;
import pl.kowalecki.dietplannerrestapi.model.ingredient.Ingredient;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meals")
public class Meal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    private LocalDateTime additionDate;

    private LocalDateTime editDate;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 5, max = 254, message = "Name must be between 5 and 254 characters")
    private String name;

    @Column(columnDefinition="TEXT")
    @Size(max = 10000, message = "Description should be shorten than 10000")
    private String description;

    @Column(columnDefinition="TEXT")
    @Size(min = 5, max = 10000, message = "Recipe should be between 5-10000")
    private String recipe;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Ingredient> ingredients;

    @Column(columnDefinition="TEXT")
    @Size(max = 10000, message = "Notes should be shorten than 10000")
    private String notes;

    @ElementCollection(targetClass = MealType.class)
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "Meal types must not be empty")
    private List<MealType> mealTypes;

    private boolean isDeleted;
    private long userId;

    @PrePersist
    protected void onCreate() {
        additionDate = LocalDateTime.now();
        isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        editDate = LocalDateTime.now();
    }


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
