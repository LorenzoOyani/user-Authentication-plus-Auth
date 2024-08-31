package org.example.jwtauth.service;

import org.example.jwtauth.entity.Token;
import org.example.jwtauth.payload.JwtResponse;
import org.example.jwtauth.payload.LoginRequest;
import org.example.jwtauth.payload.RegisterUserRequest;

public interface AuthService {

    JwtResponse login(LoginRequest loginRequest);

    String registerUser(RegisterUserRequest registerUserRequest);

    Token logout(String token);




}
