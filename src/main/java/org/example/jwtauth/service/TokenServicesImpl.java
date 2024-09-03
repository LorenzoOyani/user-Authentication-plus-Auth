package org.example.jwtauth.service;

import lombok.extern.slf4j.Slf4j;
import org.example.jwtauth.entity.RefreshedToken;
import org.example.jwtauth.entity.RefreshedTokenFactory;
import org.example.jwtauth.entity.Token;
import org.example.jwtauth.exceptions.InvalidTokenException;
import org.example.jwtauth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
public class TokenServicesImpl implements TokenServices {

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenServicesImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public boolean validateRefreshedToken(RefreshedToken token) {
        Optional<RefreshedToken> optionalRefreshToken = refreshTokenRepository.findByTokenId(token.getId());

        RefreshedToken newRefreshedToken;
        if (optionalRefreshToken.isPresent()) {
            newRefreshedToken = optionalRefreshToken.get();

            if (!newRefreshedToken.isValid()) {
                return false;
            }
            RefreshedToken refreshedToken= RefreshedTokenFactory.isWithValidToken(true);
            refreshTokenRepository.save(refreshedToken);

            return false;
        }
        return false;

    }

    @Override
    public void checkAndInvalidate(String token) {
        RefreshedToken tokenDb = refreshTokenRepository.findByToken(token);
        if (tokenDb == null) {
            log.info("Invalid token ");
            throw new InvalidTokenException("cannot Invalidate Token");
        }
        if(tokenDb.getExpiringDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(tokenDb);
            log.info("token {}, already deleted!", tokenDb);
        }


    }

    @Override
    public boolean isValidToken(String token) {
        RefreshedToken currentToken = refreshTokenRepository.findByToken(token);
        if (currentToken == null) return false;
        return currentToken.isValid();

    }
}
