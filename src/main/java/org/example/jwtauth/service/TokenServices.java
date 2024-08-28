package org.example.jwtauth.service;

import org.example.jwtauth.entity.RefreshedToken;

public interface TokenServices {

    boolean validateToken(RefreshedToken token);
}
