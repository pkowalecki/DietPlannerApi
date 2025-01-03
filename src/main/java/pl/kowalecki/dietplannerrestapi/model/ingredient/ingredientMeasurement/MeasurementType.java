package pl.kowalecki.dietplannerrestapi.model.ingredient.ingredientMeasurement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.kowalecki.dietplannerrestapi.exception.MeasurementTypeNotFoundException;

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
        throw new MeasurementTypeNotFoundException("Measurement with name " + name + " not found");
    }

    public static MeasurementType getById(int id) {
        for (MeasurementType measurementType : values()) {
            if (measurementType.getId() == id) return measurementType;
        }
        throw new MeasurementTypeNotFoundException("Measurement with id " + id + " not found");
    }


}
