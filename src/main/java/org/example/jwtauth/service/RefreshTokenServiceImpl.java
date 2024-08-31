package org.example.jwtauth.service;

import org.example.jwtauth.Exceptions.InvalidTokenException;
import org.example.jwtauth.entity.RefreshedToken;
import org.example.jwtauth.entity.User;
import org.example.jwtauth.repository.RefreshTokenRepository;
import org.example.jwtauth.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final TokenRepository tokenRepository;
    private TokenServices tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, TokenRepository tokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenRepository = tokenRepository;
    }


    @Override
    public String updateRefreshToken(User user) {
        Optional<RefreshedToken> token = getUserById(user);

        final LocalDateTime tokenExpiringTime = LocalDateTime.now().plusHours(5);

        if (token.isPresent()) {
            RefreshedToken existingUserToken = token.get();

            if(!tokenService.validateToken(existingUserToken)){
                throw new InvalidTokenException("Invalid refreshed token provided!");
            }
            //update token if is active!
            if (existingUserToken.isActive()) {
                existingUserToken.setToken(generateNewToken());
                existingUserToken.setExpiringDate(tokenExpiringTime);

                refreshTokenRepository.save(existingUserToken);
                return existingUserToken.getToken();
            }

        }

        //else if there is no token at all, create a new token
        RefreshedToken newToken = createAnewRefreshToken(user, tokenExpiringTime);
        refreshTokenRepository.save(newToken);
        return newToken.getToken();
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
