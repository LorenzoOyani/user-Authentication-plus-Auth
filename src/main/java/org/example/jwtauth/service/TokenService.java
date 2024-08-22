package org.example.jwtauth.service;

import org.example.jwtauth.payload.LoginRequest;
import org.example.jwtauth.payload.RegisterUserRequest;
import org.springframework.security.core.Authentication;

public interface TokenService {

    String generateToken(Authentication authentication);

    String getUsernameFromToken(String token);

    boolean validateToken(String token);

    void revokedToken(String token);

    String refreshToken(String oldToken, String newToken);
}
