package com.user_management.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

public interface JWTService {
    String extractUserName(String token);
    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token , UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extractClaims, UserDetails userDetails);

//    String generateRefreshToken(HashMap<String, Object> extractClaims, Object userData);

//    String generateToken(Object userData);
}
