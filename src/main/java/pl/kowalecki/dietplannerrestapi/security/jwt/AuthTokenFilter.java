package pl.kowalecki.dietplannerrestapi.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsImpl;
import pl.kowalecki.dietplannerrestapi.services.UserDetailsServiceImpl;

import java.io.IOException;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AuthJwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String jwt = parseJwt(request);
                if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                    String userEmail = jwtUtils.getEmailFromJwtToken(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    String refreshToken = jwtUtils.getJwtFromCookies(request);
                    if (refreshToken != null && jwtUtils.validateJwtToken(refreshToken)) {
                        String userEmail = jwtUtils.getEmailFromJwtToken(refreshToken);
                        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(userEmail);

                        ResponseCookie newJwtCookie = jwtUtils.generateJwtCookie(userDetails);
                        ResponseCookie newRefreshTokenCookie = jwtUtils.getRefreshTokenCookie(userDetails);

                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);

                        response.addHeader(HttpHeaders.SET_COOKIE, newJwtCookie.toString());
                        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());
                    }
                }
            }

        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: JWT token is expired");
            return;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: JWT token is unsupported");
            return;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Invalid JWT token");
            return;
        } catch (SignatureException e) {
            log.error("JWT signature validation failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: JWT signature validation failed");
            return;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: JWT claims string is empty");
            return;
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        return jwt;
    }
}
