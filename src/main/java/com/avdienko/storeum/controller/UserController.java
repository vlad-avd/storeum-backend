package com.avdienko.storeum.controller;

import com.avdienko.storeum.model.User;
import com.avdienko.storeum.repository.UserRepository;
import com.avdienko.storeum.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import static com.avdienko.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id/*, Principal principal*/) {
        // log.info(principal.getName());
        Optional<User> user = userRepository.findById(id);
        String notFoundBody = "User was not found, id=" + id;
        return HttpUtil.okOrNotFound(user, notFoundBody);
    }

    @PutMapping("/users/{id}")
    public void editUser(@PathVariable Long id) {

    }
}
