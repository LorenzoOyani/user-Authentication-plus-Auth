package org.example.jwtauth.service;

import org.example.jwtauth.payload.LoginRequest;
import org.example.jwtauth.payload.RegisterUserRequest;

public interface AuthService {

    String login(LoginRequest loginRequest);

    String registerUser(RegisterUserRequest registerUserRequest);




}
