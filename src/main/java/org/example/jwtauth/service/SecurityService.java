package org.example.jwtauth.service;

import org.example.jwtauth.entity.CustomUserDetails;
import org.example.jwtauth.entity.enums.TokenClaims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    public String getUserWithEmailFromAuth() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserDetails)
                .map(jwt -> ((Jwt) jwt).getClaimAsString(TokenClaims.USER_EMAIL.getValue()))
                .filter(username -> !"anonymous".equals(username))
                .orElse(null);
    }
}
