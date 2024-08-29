package pl.kowalecki.dietplannerrestapi.model.DTO.User;

import lombok.*;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String nickName;
    private List<String> roles = new ArrayList<>();

    public UserDTO(UserDetailsImpl userDetails) {
        this.id = userDetails.getId();
        this.email = userDetails.getEmail();
        this.name = userDetails.getName();
        this.nickName = userDetails.getNickName();
        this.surname = userDetails.getSurname();
        this.roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
