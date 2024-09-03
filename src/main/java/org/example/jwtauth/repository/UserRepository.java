package org.example.jwtauth.repository;

import org.example.jwtauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u from User u where u.email=:email")
    Optional<User> findByUsernameOrEmail(String username, String  email);

    Optional<User>  findUserByEmail(String email);

    User findByUsername(String username);


}
