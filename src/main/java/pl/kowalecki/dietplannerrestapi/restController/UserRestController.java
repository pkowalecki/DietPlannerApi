package pl.kowalecki.dietplannerrestapi.restController;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kowalecki.dietplannerrestapi.model.DTO.User.UserDTO;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsImpl;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserRestController {

    private final UserDetailsServiceImpl userService;

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable("email") String email) {
        UserDetails userDetails = userService.loadUserByUsername(email);

        if (userDetails != null) {
            UserDTO userDTO = new UserDTO((UserDetailsImpl) userDetails);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
