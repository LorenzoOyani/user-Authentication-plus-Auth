package org.example.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.jwtauth.entity.enums.TokenStatus;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Token {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(nullable = false)
    private Instant expirationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "USER_TOKEN_FK"))
    private User user;

    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus;



}
