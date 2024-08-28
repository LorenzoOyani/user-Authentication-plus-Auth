package org.example.jwtauth.service;

import org.example.jwtauth.entity.RefreshedToken;
import org.example.jwtauth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenServicesImpl implements TokenServices {

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenServicesImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public boolean validateToken(RefreshedToken token) {
        Optional<RefreshedToken> optionalRefreshToken = refreshTokenRepository.findByTokenId(token.getId());

        //use optionals to avoid null checks and handle absent values elegantly!

        //todo
        //Refactor to leverage immutability, and elegant side-effect!
        RefreshedToken newRefreshedToken;
        if (optionalRefreshToken.isPresent()) {
            newRefreshedToken = optionalRefreshToken.get();

            if(!newRefreshedToken.isValid()){
                return false;
            }
            newRefreshedToken.setValid(true);
            refreshTokenRepository.save(newRefreshedToken);

            return false;
        }
        return false;
    }
}
