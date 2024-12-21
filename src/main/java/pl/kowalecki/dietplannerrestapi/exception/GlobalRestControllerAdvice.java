package pl.kowalecki.dietplannerrestapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        String requestURI = request.getRequestURI();
        String errorMessage = "Wystąpił nieoczekiwany błąd przy wywołaniu " + requestURI;
        if (!parameters.isEmpty()) {
            errorMessage += " z parametrami: " + parameters.toString();
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "GENERAL_ERROR",
                "Wystąpił nieoczekiwany błąd.",
                new GregorianCalendar().getTimeInMillis(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        log.error("Error: {}", e.getMessage(), e);
        log.error(errorMessage);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        String requestURI = request.getRequestURI();
        String errorMessage = "Wystąpił błąd przy wywołaniu " + requestURI;
        if (!parameters.isEmpty()) {
            errorMessage += " z parametrami: " + parameters.toString();
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "PATH_NOT_FOUND",
                "Nie znaleziono zasobu.",
                new GregorianCalendar().getTimeInMillis(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        log.error("Error: {}", e.getMessage(), e);
        log.error(errorMessage);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleObjectAlreadyExistsException(ObjectAlreadyExistsException e, HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        String requestURI = request.getRequestURI();
        String errorMessage = "Wystąpił błąd przy wywołaniu " + requestURI;
        if (!parameters.isEmpty()) {
            errorMessage += " z parametrami: " + parameters.toString();
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "OBJECT_ALREADY_EXISTS",
                "Obiekt już istnieje.",
                new GregorianCalendar().getTimeInMillis(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        log.error("Error: {}", e.getMessage(), e);
        log.error(errorMessage);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        String requestURI = request.getRequestURI();
        String errorMessage = "Wystąpił błąd przy wywołaniu " + requestURI;
        if (!parameters.isEmpty()) {
            errorMessage += " z parametrami: " + parameters.toString();
        }

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "DATA_INTEGRITY_ERROR",
                "Naruszono integralność danych.",
                new GregorianCalendar().getTimeInMillis(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        log.error("Error: {}", e.getMessage(), e);
        log.error(errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        String requestURI = request.getRequestURI();
        String errorMessage = "Błąd walidacji danych w " + requestURI;
        if (!parameters.isEmpty()) {
            errorMessage += " z parametrami: " + parameters.toString();
        }

        StringBuilder validationErrors = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error ->
                validationErrors.append(error.getDefaultMessage()).append("; ")
        );

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                "VALIDATION_ERROR",
                validationErrors.toString(),
                new GregorianCalendar().getTimeInMillis(),
                e.getMessage(),
                Arrays.toString(e.getStackTrace())
        );

        log.error("Validation Error: {}", e.getMessage(), e);
        log.error(errorMessage);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
