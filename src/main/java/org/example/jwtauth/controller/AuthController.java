package org.example.jwtauth.controller;

import org.example.jwtauth.payload.RegisterUserRequest;
import org.example.jwtauth.payload.TokenResponse;
import org.example.jwtauth.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class  AuthController {

    private final TokenService  tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest){
        String  token=tokenService.registerUser(registerUserRequest);

        TokenResponse tokenResponse = new TokenResponse();

        tokenResponse.setAccessToken(token);

        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

    }



}
