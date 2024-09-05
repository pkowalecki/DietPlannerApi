package pl.kowalecki.dietplannerrestapi.restController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kowalecki.dietplannerrestapi.security.jwt.AuthJwtUtils;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsImpl;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthJwtUtils authJwtUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
        try{
            String refreshToken = authJwtUtils.getJwtFromCookies(request);
            if (refreshToken != null && authJwtUtils.validateJwtToken(refreshToken)) {
                String userEmail = authJwtUtils.getEmailFromJwtToken(refreshToken);
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userEmail);
                ResponseCookie newAccessToken = authJwtUtils.generateJwtCookie(userDetails);
                response.addHeader(HttpHeaders.SET_COOKIE, newAccessToken.toString());

                return ResponseEntity.ok().body("Access token refreshed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Refresh token is invalid or expired");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to refresh token");
        }
    }
}
