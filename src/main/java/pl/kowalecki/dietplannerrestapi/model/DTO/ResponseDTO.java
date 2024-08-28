package pl.kowalecki.dietplannerrestapi.model.DTO;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO {

    private ResponseStatus status;
    private String message;
    private Map<String, ?> data;


    public enum ResponseStatus {
        OK,
        BADDATA,
        ERROR,
        UNAUTHORIZED,
        ;
    }

    public ResponseDTO(ResponseStatus status){
        this.status=status;
    }


}


