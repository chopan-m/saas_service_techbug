package com.user_management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfigs {
  @Bean
  public OpenAPI userOpenAPI(
      @Value("${openapi.service.title}") String serviceTitle,
      @Value("${openapi.service.version}") String serviceVersion,
      @Value("${openapi.service.url}") String url) {
    return new OpenAPI()
        .servers(List.of(new Server().url(url)))
        .info(new Info().title(serviceTitle).version(serviceVersion))
            .components(new io.swagger.v3.oas.models.Components()
                    .addSecuritySchemes("bearerAuth",
                            new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .bearerFormat("JWT")));
//    By not including a .addSecurityItem() in the OpenAPI configuration, you avoid applying the security requirement globally.
//            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
//            .components(new io.swagger.v3.oas.models.Components()
//                    .addSecuritySchemes("bearerAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
  }
}