package com.user_management.config;

import com.user_management.model.Role;
import com.user_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Value("#{'${allowed.origin.urls}'.split(',')}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/USER-MANAGEMENT/v3/**").permitAll()
                        .requestMatchers("/USER-MANAGEMENT/swagger-ui/**").permitAll()
                        .requestMatchers("/USER-MANAGEMENT-DEV/v3/**").permitAll()
                        .requestMatchers("/USER-MANAGEMENT-DEV/swagger-ui/**").permitAll()
                        .requestMatchers("/test/unauth/**").permitAll()
                        .requestMatchers("/api/v1/user/profile/download/**").permitAll()
                        .requestMatchers("/api/v1/admin").hasAnyAuthority(Role.ROLE_ADMIN.name())
                        .requestMatchers("/api/v1/user").hasAnyAuthority(Role.ROLE_USER.name())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//        http.cors(cors -> cors.configurationSource(request -> {
//            CorsConfiguration configuration = new CorsConfiguration();
////            configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//            configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//            configuration.setAllowedMethods(Arrays.asList("http://localhost:4200"));
//            configuration.setAllowedHeaders(Arrays.asList("http://localhost:4200"));
//            return configuration;
//        }));
                return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
//       configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8082"));
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider  authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService.userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


}
