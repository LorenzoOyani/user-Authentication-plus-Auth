package org.example.jwtauth.service;

import org.springframework.security.core.Authentication;

public interface JwtTokenProviderService {

    String generateToken(Authentication authentication);

    String getUsernameFromToken(String token);

    boolean validateToken(String token);

    String refreshToken(String oldToken, String newToken);
}
