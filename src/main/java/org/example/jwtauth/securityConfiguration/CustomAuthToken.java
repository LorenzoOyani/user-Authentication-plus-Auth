package org.example.jwtauth.securityConfiguration;

import org.example.jwtauth.entity.CustomUserDetails;
import org.example.jwtauth.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthToken implements Authentication {

    private  User user;

    private final CustomUserDetails  customUserDetails;

    private boolean authenticated = true;

    public CustomAuthToken(User user, CustomUserDetails customUserDetails) {
        this.user = user;
        this.customUserDetails = customUserDetails;
    }

    public CustomAuthToken(CustomUserDetails    customUserDetails){
        this.customUserDetails  =customUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customUserDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public Object getDetails() {
        return customUserDetails;
    }

    @Override
    public Object getPrincipal() {
        return customUserDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }


    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated  =  isAuthenticated;
    }

    @Override
    public String getName() {
        return customUserDetails.getUsername();
    }
}
