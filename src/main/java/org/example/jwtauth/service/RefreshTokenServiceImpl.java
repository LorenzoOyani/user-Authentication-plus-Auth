package org.example.jwtauth.service;

import org.example.jwtauth.Exceptions.InvalidTokenException;
import org.example.jwtauth.entity.RefreshedToken;
import org.example.jwtauth.entity.User;
import org.example.jwtauth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public String refreshToken(User user) {
        Optional<RefreshedToken> token = getUserById(user);

        RefreshedToken refreshedToken;  //Object to persist later
        final LocalDateTime tokenExpiringTime = LocalDateTime.now().plusHours(5);

        if (token.isPresent()) {
            RefreshedToken existingUserToken = token.get();   //get the non-null value!
            if(!tokenService.validateToken(existingUserToken)){
                throw new InvalidTokenException("Invalid token provided!");
            }
            //update token if present, else generate a new one!
            if (existingUserToken.isActive()) {
                existingUserToken.setToken(generateNewToken());
                existingUserToken.setExpiringDate(tokenExpiringTime);

                refreshedToken = existingUserToken;

            } else {
                refreshedToken = createAnewRefreshToken(user, tokenExpiringTime);
            }

        } else {
            refreshedToken = createAnewRefreshToken(user, tokenExpiringTime);
        }

        refreshTokenRepository.save(refreshedToken);
        return refreshedToken.getToken();
    }

    private RefreshedToken createAnewRefreshToken(User user, LocalDateTime tokenExpiration) {
        RefreshedToken refreshedToken = new RefreshedToken();
        refreshedToken.setId(user.getId());
        refreshedToken.setUser(user);
        refreshedToken.setToken(generateNewToken());
        refreshedToken.setExpiringDate(tokenExpiration);

        return refreshedToken;
    }

    private String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    private Optional<RefreshedToken> getUserById(User user) {
        return refreshTokenRepository.findUserById(user.getId());
    }
}
