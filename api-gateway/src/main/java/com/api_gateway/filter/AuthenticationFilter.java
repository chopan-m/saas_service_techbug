package com.api_gateway.filter;

import com.api_gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter implements GatewayFilter {
    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (validator.isSecured.test(exchange.getRequest())) {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange,
                        exchange.getRequest().getPath().toString(),
                        "Authorization token not provided or invalid while accessing path -> " + exchange.getRequest().getPath(),
                        HttpStatus.UNAUTHORIZED);
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            try {
                jwtUtil.validateToken(authHeader);
            } catch (Exception e) {
                return onError(exchange,
                        exchange.getRequest().getPath().toString(),
                        "Invalid or expired token",
                        HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String path, String error, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorMessage = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", httpStatus.getReasonPhrase(), error);
        byte[] bytes = errorMessage.getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
