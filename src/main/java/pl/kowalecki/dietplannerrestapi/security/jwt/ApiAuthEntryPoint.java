package pl.kowalecki.dietplannerrestapi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pl.kowalecki.dietplannerrestapi.model.DTO.ResponseBodyDTO;


import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
public class ApiAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized " + authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseBodyDTO responseBodyDTO = ResponseBodyDTO.builder()
                .status(ResponseBodyDTO.ResponseStatus.UNAUTHORIZED)
                .message("Unauthorized: " + authException.getMessage())
                .data(Collections.singletonMap("path", request.getServletPath()))
                .build();

        log.error("API Exception body: " + responseBodyDTO.toString());
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseBodyDTO);
    }
}
