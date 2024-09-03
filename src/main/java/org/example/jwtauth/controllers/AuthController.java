package org.example.jwtauth.controllers;

import org.example.jwtauth.payload.JwtResponse;
import org.example.jwtauth.payload.LoginRequest;
import org.example.jwtauth.payload.RegisterUserRequest;
import org.example.jwtauth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest) {

        JwtResponse log = authService.login(loginRequest);

        return new ResponseEntity<>(log, HttpStatus.OK);
    }

    public ResponseEntity<?> register(RegisterUserRequest registerUserRequest) {



    }



}
