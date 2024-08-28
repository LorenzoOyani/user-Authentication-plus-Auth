package org.example.jwtauth.repository;
import org.example.jwtauth.entity.RefreshedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshedToken, Long> {

    Optional<RefreshedToken> findUserById(Long id);

    Optional<RefreshedToken> findByTokenId(Long id);

}
