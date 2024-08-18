package org.example.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

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

    private String  token;

    @Column(nullable = false)
    private Instant expirationTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id",nullable = false,  foreignKey = @ForeignKey(name = "USER_TOKEN_FK"))
    private User  user;


    Token(User user,   String  token,   Instant expirationTime){
        this.user  =user;
        this.token =token;
        this.expirationTime =  expirationTime;
    }

    Token(String  token){
        this.token=token;
    }


}
