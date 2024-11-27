package pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum MeasurementType {

    OPAKOWANIE(1,"opakowanie",""),
    PORCJA(2,"porcja",""),
    KOSTKA(3,"kostka",""),
    LYZKA(4,"łyżka",""),
    LYZECZKA(5,"łyżeczka",""),
    PUSZKA(6,"puszka",""),
    SZTUKA(7, "sztuka",""),
    KROMKA(8,"kromka",""),
    SZKLANKA(9,"szklanka",""),
    SLOIK(10,"słoik",""),
    GARSC(11,"garść",""),
    PLASTER(12,"plaster",""),
    SZCZYPTA(13,"szczypta",""),
    ZABEK(14, "ząbek","")
    ;

    private int id;
    private String namePL;
    private String nameEN;


    public static MeasurementType getMeasurementTypeByName(String name) {
        for (MeasurementType measurementType : values()) {
            if (measurementType.getNamePL().equals(name)) return measurementType;
        }
        return null;
    }


}
