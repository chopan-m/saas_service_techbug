package com.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final List<String> OPEN_API_ENDPOINTS = Arrays.asList(
        "/api/v1/auth/signup",
        "/api/v1/auth/signin",
        "/eureka/**",
        "/test/unauth/**",
        "/**/v3/api-docs/**",
        "/api/v1/user/profile/download"
    );

    public Predicate<ServerHttpRequest> isSecured =
        request -> OPEN_API_ENDPOINTS.stream()
            .noneMatch(pattern -> 
                new AntPathMatcher().match(pattern, request.getURI().getPath())
            );
}
