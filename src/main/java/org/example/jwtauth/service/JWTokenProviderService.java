package org.example.jwtauth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.example.jwtauth.entity.*;
import org.example.jwtauth.entity.enums.TokenClaims;
import org.example.jwtauth.entity.enums.TokenStatus;
import org.example.jwtauth.entity.enums.UserStatus;
import org.example.jwtauth.exceptions.InvalidTokenException;
import org.example.jwtauth.repository.RefreshTokenRepository;
import org.example.jwtauth.repository.TokenRepository;
import org.example.jwtauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Ref;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class JWTokenProviderService implements JwtTokenProviderService {
    private final long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    @Value("${app.jwt.secret}")
    private CharSequence jwtSecret;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenServices tokenService;

    public JWTokenProviderService(TokenRepository tokenRepository,
                                  UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, TokenServices tokenService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
    }

    @Override
    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();

        Date currentDate = new Date();

        long expiringTime = currentDate.getTime() + TOKEN_EXPIRATION_TIME;

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

    private Map<String, Object> getClaims(User user) {
        return Map.of(
                TokenClaims.USER_ID.getValue(), user.getId(),
                TokenClaims.USER_EMAIL.getValue(), user.getEmail(),
                TokenClaims.USER_ROLE.getValue(), user.getRoles(),
                TokenClaims.USER_STATUS.getValue(), user.getUserStatus()
        );

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
                .parseClaimsJws(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        RefreshedToken tokens = refreshTokenRepository.findByToken(token);
        if (tokens == null || !tokenService.isValidToken(token)) {
            if (tokens != null) {
                revokedToken(tokens.getId());
            }
            return false;
        }
        String username = getUsernameFromToken(token);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        user.setUserStatus(UserStatus.ACTIVE);
        tokens.setUser(user);
        refreshTokenRepository.save(tokens);//to be deleted on logout!

        return true;
    }

    @Override
    public String refreshToken(String oldToken, String newToken) {
        Instant now = Instant.now();

        Instant expiringDate = now.plusMillis(TOKEN_EXPIRATION_TIME);

        String username = this.getUsernameFromToken(oldToken);
        Map<String, Object> claims = Jwts.parser().
                setSigningKey(key())
                .build()
                .parseClaimsJws(oldToken)
                .getBody();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(expiringDate))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }

    @Override
    public Long getIdFromToken(String tokens) {
        String idValue = extractClaims(TokenClaims.JWT_ID.getValue()).toString();
        return Long.parseLong(idValue);
    }

    private Claims extractClaims(String value) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(value)
                .getBody();

    }

    public void revokedToken(Long tokens) {
        Token token = tokenRepository.findTokenById(tokens)
                .orElseThrow(() -> new RuntimeException("token not found"));
        token.setTokenStatus(TokenStatus.REVOKE);
        tokenRepository.save(token);
    }


    public void deleteExpiredToken(Long tokens) {
        Optional<RefreshedToken> token = refreshTokenRepository.findByTokenId(tokens);

        RefreshedToken newTokenObject;

        if (token.isPresent()) {
            newTokenObject = token.get();
            if (!this.validateToken(newTokenObject.getToken())) {
                throw new InvalidTokenException("token revoked!");

            }
            Instant now = Instant.now();

            Instant expiringDate = now.plus(24, ChronoUnit.HOURS);

            if (newTokenObject.getExpiringDate().isBefore(expiringDate)) {
                refreshTokenRepository.deleteById(newTokenObject.getId());

            }

        }


    }

    public String generateToken(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(""));
        Map<String, Object> claims = getClaims(user);
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .signWith(key())
                .issuedAt(Date.from(Instant.now()))
                .compact();
    }


    public String getBearerToken(String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
