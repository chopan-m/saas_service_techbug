package com.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilterFactory extends AbstractGatewayFilterFactory<AuthenticationFilterFactory.Config> {

    private final AuthenticationFilter authenticationFilter;

    public AuthenticationFilterFactory(AuthenticationFilter authenticationFilter) {
        super(Config.class);
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    public String name() {
        return "AuthenticationFilter";
    }

    @Override
    public GatewayFilter apply(Config config) {
        return authenticationFilter;
    }

    public static class Config {
        // Configuration properties if needed
    }
} 