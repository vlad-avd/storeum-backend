package com.storeum.controller;

import com.storeum.model.entity.User;
import com.storeum.payload.request.EditProfileRequest;
import com.storeum.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL + "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        MDC.put("userId", String.valueOf(id));
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<User> editUser(@RequestBody EditProfileRequest request, @PathVariable Long id) {
        MDC.put("userId", String.valueOf(id));
        return ResponseEntity.ok(userService.editProfile(request, id));
    }
}
