package org.example.jwtauth.repository;

import org.example.jwtauth.entity.User;
import org.example.jwtauth.entity.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsernameOrEmail(String username, String  email);

    Optional<User>  findUserByEmail(String email);
}
