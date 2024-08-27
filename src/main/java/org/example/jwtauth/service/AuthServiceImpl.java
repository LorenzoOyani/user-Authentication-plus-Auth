package org.example.jwtauth.service;

import lombok.extern.slf4j.Slf4j;
import org.example.jwtauth.entity.CustomUserDetails;
import org.example.jwtauth.entity.Mapper.TokenTokenResponseMapper;
import org.example.jwtauth.entity.Token;
import org.example.jwtauth.entity.User;
import org.example.jwtauth.entity.enums.TokenStatus;
import org.example.jwtauth.event.UserRegistrationEvent;
import org.example.jwtauth.payload.LoginRequest;
import org.example.jwtauth.payload.RegisterUserRequest;
import org.example.jwtauth.payload.TokenResponse;
import org.example.jwtauth.repository.TokenRepository;
import org.example.jwtauth.repository.UserRepository;
import org.example.jwtauth.securityConfiguration.CustomAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;

    private final JWTokenProviderService jwTokenProviderService;

    private final PasswordEncoder  passwordEncoder;

    private final UserRepository userRepository;

    private final TokenRepository  tokenRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final CustomAuthToken  customAuthToken;

    private final TokenTokenResponseMapper  tokenTokenResponseMapper;

    @Value("${app.jwt.expiration.milliseconds}")
    private int expiringDate;


    public AuthServiceImpl(AuthenticationManager authenticationManager, JWTokenProviderService jwTokenProviderService, PasswordEncoder passwordEncoder, UserRepository userRepository, TokenRepository tokenRepository, ApplicationEventPublisher applicationEventPublisher, CustomAuthToken customAuthToken, TokenTokenResponseMapper tokenTokenResponseMapper) {
        this.authenticationManager = authenticationManager;
        this.jwTokenProviderService = jwTokenProviderService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.customAuthToken = customAuthToken;
        this.tokenTokenResponseMapper = tokenTokenResponseMapper;
    }

    @Override
    public String login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));

        if (!customAuthToken.isAuthenticated()) {
            log.error("login failed for username {}", loginRequest.getUsername());
            throw new BadCredentialsException("Authentication for username fail "+ loginRequest.getUsername());
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return jwTokenProviderService.generateToken(authentication);

    }

    @Override
    public String registerUser(RegisterUserRequest registerUserRequest) {
        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(registerUserRequest.getPassword());
        user.setEmail(passwordEncoder.encode(registerUserRequest.getPassword()));
        userRepository.save(user);

        Authentication authentication = new CustomAuthToken(new CustomUserDetails(user));
        String userToken = this.jwTokenProviderService.generateToken(authentication);

        Token token = new Token();
        token.setUser(user);
        token.setToken(userToken);
        token.setExpirationTime(Instant.now().plusMillis(expiringDate));
        token.setTokenStatus(TokenStatus.ACTIVE);






        tokenRepository.save(token);

        applicationEventPublisher.publishEvent(new UserRegistrationEvent(this, user));

        return token.getToken();

    }
}
