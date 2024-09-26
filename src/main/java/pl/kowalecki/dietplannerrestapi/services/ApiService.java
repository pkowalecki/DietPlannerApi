package pl.kowalecki.dietplannerrestapi.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseBodyDTO;

import java.util.Map;

@Service
@AllArgsConstructor
public class ApiService implements IApiService {

    @Override
    public ResponseEntity<ResponseBodyDTO> returnResponseData(ResponseBodyDTO.ResponseStatus responseStatus, HttpStatus httpStatus) {
        return returnResponse(responseStatus, null, null, httpStatus);
    }

    @Override
    public ResponseEntity<ResponseBodyDTO> returnResponseData(ResponseBodyDTO.ResponseStatus responseStatus, String responseMessage, HttpStatus httpStatus) {
        return returnResponse(responseStatus, responseMessage, null, httpStatus);
    }

    @Override
    public ResponseEntity<ResponseBodyDTO> returnResponseData(ResponseBodyDTO.ResponseStatus responseStatus, Map<String, ?> data, HttpStatus httpStatus) {
        return returnResponse(responseStatus, null, data, httpStatus);
    }

    @Override
    public ResponseEntity<ResponseBodyDTO> returnResponseData(ResponseBodyDTO.ResponseStatus responseStatus, String responseMessage, Map<String, ?> data, HttpStatus httpStatus) {
        return returnResponse(responseStatus, responseMessage, data, httpStatus);
    }

    private ResponseEntity<ResponseBodyDTO> returnResponse(ResponseBodyDTO.ResponseStatus responseStatus, String responseMessage, Map<String, ?> data, HttpStatus httpStatus) {
        ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
                .status(responseStatus)
                .message(responseMessage != null ? responseMessage : "")
                .data(data != null ? data : Map.of())
                .build();

        return new ResponseEntity<>(responseBodyDTO, httpStatus);
    }
}
