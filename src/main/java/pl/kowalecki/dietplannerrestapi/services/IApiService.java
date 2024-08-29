package pl.kowalecki.dietplannerrestapi.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseDTO;

import java.util.Map;

public interface IApiService {
    ResponseEntity<ResponseDTO> returnResponseData(ResponseDTO.ResponseStatus responseStatus, String s, Map<String, ?> data, HttpStatus httpStatus);
}
