package org.example.jwtauth.service;

import org.example.jwtauth.exceptions.InvalidTokenException;
import org.example.jwtauth.entity.RefreshedToken;
import org.example.jwtauth.entity.User;
import org.example.jwtauth.repository.RefreshTokenRepository;
import org.example.jwtauth.repository.TokenRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private TokenServices tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @Override
    public String updateRefreshToken(User user) {
        Optional<RefreshedToken> token = getUserById(user);
        Instant now = Instant.now();

        Instant tokenExpiringTime  =  now.plus(24,  ChronoUnit.HOURS);

        if (token.isPresent()) {
            RefreshedToken existingUserToken = token.get();
            if (!tokenService.validateRefreshedToken(existingUserToken)) {
                throw new InvalidTokenException("Invalid refreshed token provided!");
            }
            if (existingUserToken.isActive()) {
                existingUserToken.setToken(generateNewToken());
                existingUserToken.setExpiringDate(tokenExpiringTime);

                refreshTokenRepository.save(existingUserToken);
                return existingUserToken.getToken();
            }

        }
        RefreshedToken newToken = createAnewRefreshToken(user, LocalDateTime.from(tokenExpiringTime));
        refreshTokenRepository.save(newToken);
        return newToken.getToken();
    }

    private RefreshedToken createAnewRefreshToken(User user, LocalDateTime tokenExpiration) {
        RefreshedToken refreshedToken = new RefreshedToken();
        refreshedToken.setId(user.getId());
        refreshedToken.setUser(user);
        refreshedToken.setToken(generateNewToken());
        refreshedToken.setExpiringDate(Instant.from(tokenExpiration));

        return refreshedToken;
    }

    private String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    private Optional<RefreshedToken> getUserById(User user) {
        return refreshTokenRepository.findUserById(user.getId());
    }
}
