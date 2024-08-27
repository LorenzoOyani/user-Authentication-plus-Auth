package org.example.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.jwtauth.entity.enums.Roles;
import org.example.jwtauth.entity.enums.TokenClaims;
import org.example.jwtauth.entity.enums.UserStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User  extends BaseEntity  {

    @Id
    private int id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    private String username;
    @Column(nullable = false)

    private String email;
    @Column(nullable = false)

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    @Enumerated(EnumType.STRING)
    private UserStatus  userStatus;


//    public String getName(){
//        return  String.join(" ", firstname, lastname);
//    }


    /**
     * Claims carry information about the user, like name, status, expiration date.
     * */

//    public Map<String, Object>  getClaims(){
//        Map<String, Object> claims =new HashMap<>();
//        claims.put(TokenClaims.JWT_ID.getValue(), this.id);
//        claims.put(TokenClaims.USER_STATUS.getValue(),  this.userStatus);
//        claims.put(TokenClaims.USER_NAME.getValue(), this.username);
//        claims.put(TokenClaims.USER_EMAIL.getValue(),   this.email);
//
//
//
//        return claims;
//    }


}
