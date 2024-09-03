package org.example.jwtauth.service;

import org.example.jwtauth.entity.RefreshedToken;
import org.example.jwtauth.entity.Token;

public interface TokenServices {

    boolean validateRefreshedToken(RefreshedToken token);

    void checkAndInvalidate(String token);

    boolean isValidToken(String token);

}
