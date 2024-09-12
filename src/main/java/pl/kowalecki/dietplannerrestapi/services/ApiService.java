package pl.kowalecki.dietplannerrestapi.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseBodyDTO;

import java.util.Map;

@Service
@AllArgsConstructor
public class ApiService implements IApiService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Override
    public ResponseEntity<ResponseBodyDTO> returnResponseData(ResponseBodyDTO.ResponseStatus responseStatus, String responseMessage, Map<String, ?> data, HttpStatus httpStatus) {
        new ResponseBodyDTO();
        ResponseBodyDTO responseBodyDTO = switch (responseStatus) {
            case OK, BAD_DATA, UNAUTHORIZED, ERROR -> ResponseBodyDTO.builder()
                    .status(responseStatus)
                    .message(responseMessage)
                    .data(data)
                    .build();
        };

        return new ResponseEntity<>(responseBodyDTO, httpStatus);
    }
}
