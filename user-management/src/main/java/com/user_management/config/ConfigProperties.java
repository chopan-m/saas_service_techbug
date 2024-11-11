package com.user_management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class ConfigProperties {
    private String env;
    
    // Getters and Setters
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
