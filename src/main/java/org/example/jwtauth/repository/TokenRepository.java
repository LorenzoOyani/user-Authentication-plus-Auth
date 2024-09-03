package org.example.jwtauth.repository;

import org.example.jwtauth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTokenById(Long id);

    Token findTokenByToken(String token);




}
