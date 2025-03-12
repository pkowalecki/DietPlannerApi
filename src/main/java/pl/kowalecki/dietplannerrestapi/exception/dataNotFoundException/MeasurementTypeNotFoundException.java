package pl.kowalecki.dietplannerrestapi.exception.dataNotFoundException;

public class MeasurementTypeNotFoundException extends DataNotFoundException  {
    public MeasurementTypeNotFoundException(String message) {
        super(message);
    }
}
