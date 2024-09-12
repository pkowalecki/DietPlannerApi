package pl.kowalecki.dietplannerrestapi.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseBodyDTO;

import java.util.Map;

public interface IApiService {
    ResponseEntity<ResponseBodyDTO> returnResponseData(ResponseBodyDTO.ResponseStatus responseStatus, String responseMessage, Map<String, ?> data, HttpStatus httpStatus);
}
