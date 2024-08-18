package org.example.jwtauth.entity;
import org.example.jwtauth.config.JWTokenProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public  class CustomUserDetails implements UserDetails {

    private final User user;
    private  JWTokenProvider   jwTokenProvider;

    public CustomUserDetails(User user, JWTokenProvider jwTokenProvider) {
        this.user = user;
        this.jwTokenProvider = jwTokenProvider;
    }

    public CustomUserDetails(User user){
        this.user=user;
    }

    public Map<String,  Object> getClaims(){
        return user.getClaims();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRoles().toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return  UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return  UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return  UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
