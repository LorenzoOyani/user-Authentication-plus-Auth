package org.example.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.jwtauth.entity.enums.Roles;
import org.example.jwtauth.entity.enums.TokenClaims;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private int id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)

    private String username;
    @Column(nullable = false)

    private String email;
    @Column(nullable = false)

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Token> token;


    public Map<String, Object>  getClaims(){
        Map<String, Object> claims =new HashMap<>();
        claims.put(TokenClaims.JTI.getValue(),  "ID");
        claims.put(TokenClaims.AUDIENCE.getValue(), "aud");
        claims.put(TokenClaims.ISSUED_AT.getValue(), "iss_at");
        claims.put(TokenClaims.EXPIRATION.getValue(), "exp");
        claims.put(TokenClaims.ISSUER.getValue(), "iss");
        claims.put(TokenClaims.SUBJECT.getValue(), "sub");

        return claims;
    }


}
