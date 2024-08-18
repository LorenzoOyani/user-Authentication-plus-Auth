package org.example.jwtauth.entity;

import org.example.jwtauth.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> users = userRepository.findByUsernameOrEmail(username, username);

        User  user=null;
        if(users.isPresent()){
            user = users.get();
        }
        assert user !=null;
        Set<GrantedAuthority> authoritySet = Collections
                .singleton(new SimpleGrantedAuthority(user.getRoles().toString()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                null,
                authoritySet
        );

    }
}
