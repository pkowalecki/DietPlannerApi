package pl.kowalecki.dietplannerrestapi.restController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.kowalecki.dietplannerrestapi.model.DTO.ConfirmationTokenDTO;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseBodyDTO;
import pl.kowalecki.dietplannerrestapi.services.UserServiceImpl;


import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
@AllArgsConstructor
public class RegisterConfirmationRestController {

    private UserServiceImpl userService;


    @GetMapping(value = "/confirm")
    public ResponseEntity<ResponseBodyDTO> confirmUser(@RequestParam("token") String confirmationToken){
        Map<String, String> errors = new HashMap<>();
        ConfirmationTokenDTO token = new ConfirmationTokenDTO(confirmationToken);

        if (token.getToken() != null){
            boolean isActivated = userService.findAndActivateUserByHash(token.getToken());
            if (isActivated){
                return new ResponseEntity<>(HttpStatus.OK);
            }else {
                errors.put("ACTIVATED", "Account is already active");
                ResponseBodyDTO response = ResponseBodyDTO.builder()
                        .status(ResponseBodyDTO.ResponseStatus.ERROR)
                        .data(errors)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }else {
            errors.put("INVALID_TOKEN", "Invalid token");
            ResponseBodyDTO response = ResponseBodyDTO.builder()
                    .status(ResponseBodyDTO.ResponseStatus.ERROR)
                    .data(errors)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
