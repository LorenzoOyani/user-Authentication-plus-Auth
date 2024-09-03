package org.example.jwtauth.entity;

import java.time.Instant;

public class RefreshedTokenFactory {

    private static User user;

    private static RefreshedToken refreshedToken;

    public static RefreshedToken isWithValidToken(boolean Valid){
        return RefreshedToken.builder()
                .id(1L)
                .user(user)
                .token(refreshedToken.getToken())
                .expiringDate(Instant.now().plusMillis(1000 * 60 * 24))
                .isValid(Valid)
                .isValid(true)
                .refreshedTokenStatus(refreshedToken.getRefreshedTokenStatus())
                .build();
    }
}
