package pl.kowalecki.dietplannerrestapi.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseDTO;

import java.util.Map;

@Service
@AllArgsConstructor
public class ApiService implements IApiService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Override
    public ResponseEntity<ResponseDTO> returnResponseData(ResponseDTO.ResponseStatus responseStatus, String s, Map<String, ?> data, HttpStatus httpStatus) {
        ResponseDTO responseDTO = new ResponseDTO();
        switch (responseStatus) {
            case OK:
                responseDTO = ResponseDTO.builder().build();
                break;
            case BADDATA:
                responseDTO = ResponseDTO.builder().build();
                break;
            case UNAUTHORIZED:
                responseDTO = ResponseDTO.builder().build();
                break;
            case ERROR:
                responseDTO = ResponseDTO.builder().build();
                break;
        }

        return new ResponseEntity<>(responseDTO, httpStatus);
    }
}
