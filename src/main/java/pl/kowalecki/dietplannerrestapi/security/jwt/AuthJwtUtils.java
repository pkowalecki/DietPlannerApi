package pl.kowalecki.dietplannerrestapi.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import pl.kowalecki.dietplannerrestapi.model.User;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsImpl;
import pl.kowalecki.dietplannerrestapi.services.UserService;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class AuthJwtUtils {

    @Value("${dietplanner.app.jwtSecret}")
    private String jwtSecret;

    @Value("${dietplanner.app.jwtExpiration}")
    private int jwtExpiration;

    @Value("${dietplanner.app.jwtCookieName}")
    private String jwtCookie;

    @Value("${dietplanner.app.jwtRefreshExpiration}")
    private int jwtRefreshExpiration;

    @Value("${dietplanner.app.jwtRefreshCookieName}")
    private String jwtRefCookie;

    @Autowired
    private UserService userService;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        return cookie != null ? cookie.getValue() : null;
    }

    private String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshExpiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }


    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromEmail(userPrincipal.getEmail());
        return ResponseCookie.from(jwtCookie, jwt)
                .path("/")
                .maxAge(jwtExpiration / 1000)
                .httpOnly(true)
//                .secure(true) // HTTPS
                .build();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public ResponseCookie getRefreshTokenCookie(UserDetailsImpl userPrincipal) {
        String refreshToken = generateRefreshTokenFromEmail(userPrincipal.getEmail());
        return ResponseCookie.from(jwtRefCookie, refreshToken)
                .path("/")
                .maxAge(jwtRefreshExpiration / 1000)
                .httpOnly(true)
//                .secure(true) // HTTPS
                .build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie
                .from(jwtCookie, null)
                .maxAge(0)
                .path("/")
                .build();
    }

    public ResponseCookie getCleanRefreshToken() {
        return ResponseCookie
                .from(jwtRefCookie, null)
                .maxAge(0)
                .path("/")
                .build();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Integer getUserIdFromToken(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);
        String userEmail = getEmailFromJwtToken(jwt);
        Optional<User> user = userService.findByEmail(userEmail);
        return user.map(User::getId).orElse(null);
    }
}
