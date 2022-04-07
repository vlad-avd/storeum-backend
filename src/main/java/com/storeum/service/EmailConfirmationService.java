package com.storeum.service;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.ValidationResult;
import com.storeum.model.entity.EmailConfirmToken;
import com.storeum.model.entity.User;
import com.storeum.payload.response.GenericResponse;
import com.storeum.repository.EmailConfirmTokenRepository;
import com.storeum.validator.EmailConfirmationValidator;
import com.storeum.model.ValidationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConfirmationService {

    private final EmailConfirmTokenRepository tokenRepository;
    private final UserService userService;
    private final EmailConfirmationValidator validator;

    public EmailConfirmToken getByToken(String token) {
        log.info("Trying to get email confirmation token, token={}", token);
        String errorMessage = String.format("Email confirmation token was not found in DB, value=%s", token);
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }

    public EmailConfirmToken createToken(Long userId) {
        EmailConfirmToken token = EmailConfirmToken.builder()
                .token(UUID.randomUUID().toString())
                .user(userService.getUserById(userId))
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .isConfirmed(false)
                .build();
        log.info("Email confirmation token successfully created");

        return tokenRepository.save(token);
    }

    @Transactional
    public GenericResponse<String> confirmEmail(String token) {
        EmailConfirmToken tokenEntity = getByToken(token);
        ValidationResult validationResult = validator.validate(tokenEntity);

        if (ValidationStatus.FAIL == validationResult.getValidationStatus()) {
            log.info("Email confirmation failed, cause={}", validationResult.getErrorMessage());
            return new GenericResponse<>(validationResult.getErrorMessage());
        }

        tokenEntity.setConfirmed(true);
        tokenRepository.save(tokenEntity);
        User user = tokenEntity.getUser();
        user.setEnabled(true);
        userService.save(user);
        log.info("Email was confirmed, tokenEntity={} ", tokenEntity);

        return new GenericResponse<>("Email successfully confirmed", HttpStatus.OK);
    }
}
