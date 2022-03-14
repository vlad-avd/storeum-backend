package com.avdienko.storeum.controller;

import com.avdienko.storeum.model.entity.User;
import com.avdienko.storeum.payload.request.LoginRequest;
import com.avdienko.storeum.payload.request.LogoutRequest;
import com.avdienko.storeum.payload.request.RefreshTokenRequest;
import com.avdienko.storeum.payload.request.RegisterRequest;
import com.avdienko.storeum.payload.response.GenericResponse;
import com.avdienko.storeum.payload.response.JwtResponse;
import com.avdienko.storeum.payload.response.RefreshTokenResponse;
import com.avdienko.storeum.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.avdienko.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL + "/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.auth(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        GenericResponse<User> response = authService.register(request);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getResponseBody());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@Valid @RequestBody LogoutRequest request) {
        String response = authService.logout(request);
        return ResponseEntity.ok(response);
    }
}