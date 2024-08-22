package org.example.jwtauth.controller;

import org.example.jwtauth.payload.RegisterUserRequest;
import org.example.jwtauth.payload.TokenResponse;
import org.example.jwtauth.service.AuthService;
import org.example.jwtauth.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        String token = authService.registerUser(registerUserRequest);

        TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setAccessToken(token);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

    }


}
