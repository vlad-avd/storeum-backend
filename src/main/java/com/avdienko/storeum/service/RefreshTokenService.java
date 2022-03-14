package com.avdienko.storeum.service;

import com.avdienko.storeum.exception.ResourceNotFoundException;
import com.avdienko.storeum.exception.TokenRefreshException;
import com.avdienko.storeum.model.entity.RefreshToken;
import com.avdienko.storeum.model.entity.User;
import com.avdienko.storeum.repository.RefreshTokenRepository;
import com.avdienko.storeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.avdienko.storeum.util.MessageFormatters.userNotFound;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${storeum.app.jwt-refresh-expiration-ms}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userNotFound(userId)));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .token(UUID.randomUUID().toString())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            String errorMessage = "Refresh token was expired. Please make a new sign in request";
            throw new TokenRefreshException(token.getToken(), errorMessage);
        }

        return token;
    }

    public void deleteByUserId(Long userId) {
        String errorMessage = String.format("There is no refresh token in DB associated with userId=%s", userId);
        refreshTokenRepository.deleteByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }
}
