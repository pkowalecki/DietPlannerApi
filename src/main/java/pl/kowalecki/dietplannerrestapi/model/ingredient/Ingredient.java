package pl.kowalecki.dietplannerrestapi.model.ingredient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import pl.kowalecki.dietplannerrestapi.model.Meal;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientAmount;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientAmount.IngredientUnit;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.IngredientMeasurement;
import pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement.MeasurementType;


import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ingredients")
@ToString
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    private Double ingredientAmount;

    @Enumerated(EnumType.STRING)
    private IngredientUnit ingredientUnit;

    private Double measurementValue;

    @Enumerated(EnumType.STRING)
    private MeasurementType measurementType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_ingredient_name")
    private IngredientName ingredientNameId;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    @JsonBackReference
    private Meal meal;


    @Transient
    public Double sumTotalAmount(double d1, double d2){
        return (d1+d2);
    }
    @Transient
    public static IngredientAmount getOnlyDoubleFromAmount(String totalAmount){
        totalAmount = totalAmount.replaceAll("\\s", "");
        String regex = "([0-9]+([,.][0-9]+)?)([a-zA-Z]+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(totalAmount);

        if (matcher.find()){
            String amount = matcher.group(1);
            String unit = matcher.group(3);

            amount = amount.replace(",", ".");
            double value = Double.parseDouble(amount);
//            return new IngredientAmount(value, unit);
        }
        return new IngredientAmount();
    }

    @Transient
    public static IngredientMeasurement getOnlyDoubleFromMeasurement(String totalAmount){
        if(totalAmount == null){
            return new IngredientMeasurement();
        }
//        totalAmount = totalAmount.replaceFirst("\\s", "");
//        String regex = "([0-9]+([,.][0-9]+)?)([a-zA-Z]+)";
//        String regex = "([0-9]+([,.][0-9]+)?)([a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+)";
        String regex = "([0-9]+([.,][0-9]+)?)\\s+(.*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(totalAmount);

        if (matcher.find()){
            String amount = matcher.group(1);
            String unit = matcher.group(3);

            amount = amount.replace(",", ".");
            double value = Double.parseDouble(amount);
//            return new IngredientMeasurement(value, "sztuki");
        }
        return new IngredientMeasurement();
    }
}
