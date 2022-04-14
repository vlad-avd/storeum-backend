package com.storeum.service;

import com.storeum.exception.RefreshTokenException;
import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.entity.RefreshToken;
import com.storeum.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${app.jwt-refresh-expiration-ms}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshToken> getRefreshToken(String token) {
        log.info("Trying to get refresh token");
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        log.info("Generating new refresh token");
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userService.getUserById(userId))
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .token(UUID.randomUUID().toString())
                .build();
        log.info("Refresh token generated successfully");
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            String errorMessage = "Refresh token was expired. Please make a new sign in request";
            throw new RefreshTokenException(token.getToken(), errorMessage);
        }

        return token;
    }

    public void deleteByUserId(Long userId) {
        log.info("Trying to delete refresh token");
        String errorMessage = String.format("There is no refresh token in DB associated with userId=%s", userId);
        refreshTokenRepository.deleteByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }
}
