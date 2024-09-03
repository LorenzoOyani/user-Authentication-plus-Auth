package org.example.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.jwtauth.entity.enums.TokenStatus;
import org.example.jwtauth.entity.enums.TokenType;

import java.time.Instant;

@Entity
@Table(name = "Token", indexes = {
        @Index(name = "idx_token_token_unq", columnList = "token", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private Instant expirationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "USER_TOKEN_FK"))
    private User user;


}
