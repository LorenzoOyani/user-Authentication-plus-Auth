package org.example.jwtauth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.jwtauth.entity.CustomUserDetails;
import org.example.jwtauth.entity.Token;
import org.example.jwtauth.entity.User;
import org.example.jwtauth.entity.enums.TokenClaims;
import org.example.jwtauth.entity.enums.TokenStatus;
import org.example.jwtauth.entity.enums.TokenType;
import org.example.jwtauth.repository.TokenRepository;
import org.example.jwtauth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class JWTokenProviderService implements JwtTokenProviderService {

    private static final Logger log = LoggerFactory.getLogger(JWTokenProviderService.class);


    @Value("${app.jwt.secret}")
    private CharSequence jwtSecret;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public JWTokenProviderService(TokenRepository tokenRepository,
                                  UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();

        Date currentDate = new Date();

        long expiringTime = currentDate.getTime() + 1000 * 60 * 60 * 24;

        Date expiringDates = new Date(currentDate.getTime() + expiringTime);

        final Map<String, Object> claims = getClaims(authentication);

        return Jwts.builder()
                .setClaims(claims)
                .subject(userName)
                .signWith(key())
                .issuedAt(Date.from(Instant.now()))
                .setExpiration(expiringDates)
                .compact();
    }

    private static Map<String, Object> getClaims(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return Map.of(
                TokenClaims.JWT_ID.getValue(), userDetails.getUserId(),
                TokenClaims.USER_EMAIL.getValue(), userDetails.getUserEmail(),
                TokenClaims.USER_NAME.getValue(), userDetails.getUsername(),
                TokenClaims.USER_ROLE.getValue(), userDetails.getAuthorities()
        );
    }

    private Map<String, Object> getClaims(String username, User user) {

    }

    public Key key() {
        byte[] key = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(key);
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
        Optional<Token> existingTokens = tokenRepository.findTokenById(token);

        if (existingTokens.isPresent()) {
            Token newTokens = existingTokens.get();

            if(newTokens.isRevoke()){
                return false; // not valid token!
            }
            newTokens.setTokenStatus(TokenStatus.ACTIVE); //valid token, hence can be validated.
            tokenRepository.save(newTokens);
            return false; // token is revoked so it is not valid
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
        long currentTime = currentDate.getTime() + 1000 * 60 * 60 * 24;
        Date expiringDate = new Date(currentTime);
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
                .setExpiration(expiringDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }

    public void revokedToken(String tokens) {
        Token token = tokenRepository.findTokenById(tokens).orElseThrow(() -> new RuntimeException("token not found"));
        token.setTokenStatus(TokenStatus.REVOKE);
        token.setTokenType(TokenType.BEARER);
        tokenRepository.save(token);
    }


    public void deleteExpiredToken(Long tokens) {
        Token token = tokenRepository.getReferenceById(tokens);

        Date currentDate = new Date();

        long expiringDate = new Date().getTime() - currentDate.getTime();
        Date timeBtwDates = new Date(currentDate.getTime() - expiringDate);

        token.setExpirationTime(timeBtwDates.toInstant());

        if (token.getExpirationTime().isAfter(Instant.now())) {
            tokenRepository.delete(token); // remove expired token from dataBase
        }

    }

    public String generateToken(String username) {
        User user =userRepository.findByUsernameOrEmail(username,   username)
                .orElseThrow(()->new UsernameNotFoundException(""));

        Map<String, Object> claims  =  getClaims(username,  user);

        return  Jwts.builder()
                .claims(claims)
                .subject(username)
                .signWith(key())
                .issuedAt(Date.from(Instant.now()))
                .compact();
    }


}
