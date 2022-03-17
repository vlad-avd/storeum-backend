package com.avdienko.storeum.service;

import com.avdienko.storeum.exception.ResourceNotFoundException;
import com.avdienko.storeum.model.entity.User;
import com.avdienko.storeum.payload.request.EditProfileRequest;
import com.avdienko.storeum.repository.UserRepository;
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

    public User editProfile(EditProfileRequest request, Long userId) {
        log.info("Edit profile request received, username={}, email={}", request.getUsername(), request.getEmail());
        User user = getUserById(userId);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        User editedUser = userRepository.save(user);
        log.info("User profile was successfully edited, user={}", editedUser);
        return editedUser;
    }
}
