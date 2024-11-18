package pl.kowalecki.dietplannerrestapi.model.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement{
    private double measurementAmount;
    private String measurementUnit;

}
