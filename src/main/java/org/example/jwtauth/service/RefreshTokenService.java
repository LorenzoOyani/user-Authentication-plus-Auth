package org.example.jwtauth.service;

import org.example.jwtauth.entity.User;

public interface RefreshTokenService {


    String refreshToken(User user);


}
