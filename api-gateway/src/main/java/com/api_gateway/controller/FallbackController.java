package com.api_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import com.api_gateway.dto.GenericResponse;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    
    @GetMapping("/user-service")
    public Mono<GenericResponse<String>> userServiceFallback() {
        return Mono.just(GenericResponse.build(
            "/fallback/user-service",
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            false,
            "Service Unavailable",
            "User Service is taking too long to respond or is down. Please try again later",
            null
        ));
    }

    @GetMapping("/default")
    public Mono<GenericResponse<String>> defaultFallback() {
        return Mono.just(GenericResponse.build(
            "/fallback/default",
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            false,
            "Service Unavailable",
            "The requested service is currently unavailable. Please try again later",
            null
        ));
    }
}
