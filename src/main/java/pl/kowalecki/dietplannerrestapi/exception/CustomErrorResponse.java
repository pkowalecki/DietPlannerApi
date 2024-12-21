package pl.kowalecki.dietplannerrestapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomErrorResponse {
    private String errorCode;
    private String errorMessage;
    private long timestamp;
    private String details;
    private String stackTrace;
}
