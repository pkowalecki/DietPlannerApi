package pl.kowalecki.dietplannerrestapi.configuration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class UserContextFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        String email = exchange.getRequest().getHeaders().getFirst("X-User-Email");

        if (userId != null && email != null) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList()));
            exchange.getAttributes().put("userId", userId);
        }
        return chain.filter(exchange);
    }

    public static String getUserId(ServerWebExchange exchange) throws AuthenticationException {
        return (String) exchange.getAttributes().get("userId");
    }
}
