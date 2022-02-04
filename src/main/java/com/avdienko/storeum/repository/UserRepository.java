package com.avdienko.storeum.repository;

import com.avdienko.storeum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String uname);

    Boolean existsByUsername(String uname);

    Boolean existsByEmail(String email);
}
