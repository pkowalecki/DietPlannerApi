package pl.kowalecki.dietplannerrestapi.exception;

public class MeasurementTypeNotFoundException extends RuntimeException {
    public MeasurementTypeNotFoundException(String message) {
        super(message);
    }
}
