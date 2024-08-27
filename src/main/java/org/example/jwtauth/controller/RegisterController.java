package org.example.jwtauth.controller;

import org.example.jwtauth.entity.Mapper.TokenTokenResponseMapper;
import org.example.jwtauth.entity.Token;
import org.example.jwtauth.payload.RegisterUserRequest;
import org.example.jwtauth.payload.TokenResponse;
import org.example.jwtauth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/")
public class RegisterController {

    private final AuthService authService;

    private final TokenTokenResponseMapper tokenResponseMapper;

    public RegisterController(AuthService authService, TokenTokenResponseMapper tokenResponseMapper) {
        this.authService = authService;
        this.tokenResponseMapper = tokenResponseMapper;
    }

    @PostMapping("register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        String token = authService.registerUser(registerUserRequest);
        Token tokens = new Token();
        tokens.setToken(token);

        TokenResponse tokenResponse = tokenResponseMapper.map(tokens);


        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

    }


}
