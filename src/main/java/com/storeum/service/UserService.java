package com.storeum.service;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.entity.User;
import com.storeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        log.info("Trying to get user, id={}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id=%s was not found in DB", id))
        );
    }

    public User getUserByEmail(String email) {
        log.info("Trying to get user, email={}", email);
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with email=%s was not found in DB", email))
        );
    }

    public void save(User user) {
        userRepository.save(user);
        //TODO: when update user, it logs that last is created
        log.info("User was created, user={} ", user);
    }
}
