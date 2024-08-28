package pl.kowalecki.dietplannerrestapi.model.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfirmationTokenDTO {
    private String token;
}
