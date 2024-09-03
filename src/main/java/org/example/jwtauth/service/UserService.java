package org.example.jwtauth.service;

import org.example.jwtauth.entity.Token;
import org.example.jwtauth.payload.LoginRequest;

public interface UserService {

      Token login(LoginRequest  loginRequest);


      void logout(Token token);



}
