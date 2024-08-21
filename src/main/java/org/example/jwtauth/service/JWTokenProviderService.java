package org.example.jwtauth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.jwtauth.entity.CustomUserDetails;
import org.example.jwtauth.entity.Token;
import org.example.jwtauth.entity.User;
import org.example.jwtauth.entity.enums.TokenStatus;
import org.example.jwtauth.event.UserRegistrationEvent;
import org.example.jwtauth.payload.LoginRequest;
import org.example.jwtauth.payload.RegisterUserRequest;
import org.example.jwtauth.repository.TokenRepository;
import org.example.jwtauth.repository.UserRepository;
import org.example.jwtauth.securityConfiguration.CustomAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class JWTokenProviderService implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(JWTokenProviderService.class);

    @Value("${app.jwt.expiration.milliseconds}")
    private int expiringDate;

    @Value("${app.jwt.secret}")
    private CharSequence jwtSecret;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final AuthenticationManager authenticationManager;

    public JWTokenProviderService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();

        Date currentDate = new Date();

        Date expiringDates = new Date(currentDate.getTime() + expiringDate);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> claims = userDetails.getClaims();

        return Jwts.builder()
                .setClaims(claims)
                .subject(userName)
                .issuedAt(new Date())
                .expiration(expiringDates)
                .signWith(key())
                .compact();
    }

    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    @Override
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    @Override
    public boolean validateToken(String token) {
        Optional<Token> tokens = tokenRepository.findTokenById(token);
        if (tokens.isPresent()) {
            Token token1 = tokens.get();
            revokedToken(token1.toString());
            return false;
        }
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);

            return true;
        } catch (Exception e) {
            log.info("error validating this token with message {}", e.getMessage());
            return false;

        }
    }

    @Override
    public String refreshToken(String oldToken, String newToken) {
        //todo
        //extract the claims from the old token,
        // create a new token with new expiration date and sign in with same key
        Date currentDate = new Date();
        String username = this.getUsernameFromToken(oldToken);
        Map<String, Object> claims = Jwts.parser().
                setSigningKey(key())
                .build()
                .parseClaimsJws(oldToken)
                .getBody();

        //use builders`when you want to  generate a new token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(currentDate.getTime() + expiringDate))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }

    @Override
    public String login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));

        if (!authentication.isAuthenticated()) {
            // do something!
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return this.generateToken(authentication);
    }

    @Override
    public String registerUser(RegisterUserRequest registerUserRequest) {
        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(registerUserRequest.getPassword());
        user.setEmail(passwordEncoder.encode(registerUserRequest.getPassword()));
        userRepository.save(user);

        Authentication authentication = new CustomAuthToken(new CustomUserDetails(user));
        String userToken = this.generateToken(authentication);

        Token token = new Token();
        token.setUser(user);
        token.setToken(userToken);
        token.setExpirationTime(Instant.now().plusMillis(expiringDate));
        token.setTokenStatus(TokenStatus.ACTIVE);

        tokenRepository.save(token);

        applicationEventPublisher.publishEvent(new UserRegistrationEvent(this, user));

        return token.getToken();

    }

    public void revokedToken(String tokens) {
        Token token = tokenRepository.findTokenById(tokens).orElseThrow(() -> new RuntimeException("token not found"));

        token.setTokenStatus(TokenStatus.REVOKE);
        tokenRepository.save(token);
    }

    public void deleteExpiredToken(Long tokens) {
        Token token = tokenRepository.getReferenceById(tokens);

        Date currentDate = new Date();

        long expiringDate = new Date().getTime() - currentDate.getTime();
        Date timeBtwDates = new Date(currentDate.getTime() - expiringDate);

        token.setExpirationTime(timeBtwDates.toInstant());

        if (token.getExpirationTime().isBefore(Instant.now())) {
            tokenRepository.delete(token); // remove expired token from dataBase
        }

    }
}
