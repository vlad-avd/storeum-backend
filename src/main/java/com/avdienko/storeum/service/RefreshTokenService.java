package com.avdienko.storeum.service;

import com.avdienko.storeum.exception.TokenRefreshException;
import com.avdienko.storeum.model.entity.RefreshToken;
import com.avdienko.storeum.model.entity.User;
import com.avdienko.storeum.repository.RefreshTokenRepository;
import com.avdienko.storeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

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
        Optional<User> user = userRepository.findById(userId);

        RefreshToken refreshToken = new RefreshToken();
        //TODO: create custom exception
        user.ifPresentOrElse(refreshToken::setUser, () -> {throw new RuntimeException();});
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign in request");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
//        Optional<User> user = userRepository.findById(userId);
        refreshTokenRepository.deleteByUserId(userId);
        //TODO: create custom exception
//        user.ifPresentOrElse(refreshTokenRepository::deleteByUser, () -> {throw new RuntimeException();});
    }
}
