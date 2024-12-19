package pl.kowalecki.dietplannerrestapi.model.ingredient;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "ingredients_name", indexes = {
        @Index(name="idx_ingredient_name", columnList = "name")
})
public class IngredientName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 254, message = "Name must be between 3 and 254 characters")
    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    @Size(min = 2, max = 254, message = "Name must be between 2 and 254 characters")
    private String brand;

    @Column(name = "protein")
    @DecimalMin(value = "0.0", inclusive = true, message = "Value should be greater than or equal to 0")
    @DecimalMax(value = "1000.0", inclusive = true, message = "Value should be less than or equal to 1000")
    private double protein;

    @Column(name = "carbohydrates")
    @DecimalMin(value = "0.0", inclusive = true, message = "Value should be greater than or equal to 0")
    @DecimalMax(value = "1000.0", inclusive = true, message = "Value should be less than or equal to 1000")
    private double carbohydrates;

    @Column(name = "fat")
    @DecimalMin(value = "0.0", inclusive = true, message = "Value should be greater than or equal to 0")
    @DecimalMax(value = "1000.0", inclusive = true, message = "Value should be less than or equal to 1000")
    private double fat;

    @Column(name = "kcal")
    @DecimalMin(value = "0.0", inclusive = true, message = "Value should be greater than or equal to 0")
    @DecimalMax(value = "9999.9", inclusive = true, message = "Value should be less than or equal to 9999")
    private double kcal;

    @NotNull(message = "User cannot be null")
    private Long userId;

}
