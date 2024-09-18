package pl.kowalecki.dietplannerrestapi.model.ingredient;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ingredients_name", indexes = {
        @Index(name="idx_ingredient_name", columnList = "name")
})
public class IngredientName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "brand")
    private String brand;
    @Column(name = "protein")
    private int protein;
    @Column(name = "carbohydrates")
    private int carbohydrates;
    @Column(name = "fat")
    private int fat;
    @Column(name = "kcal")
    private int kcal;

}
