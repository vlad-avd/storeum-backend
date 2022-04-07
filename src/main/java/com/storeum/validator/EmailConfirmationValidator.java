package com.storeum.validator;

import com.storeum.model.ValidationResult;
import com.storeum.model.entity.EmailConfirmToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailConfirmationValidator {

    public ValidationResult validate(EmailConfirmToken token) {
        if (token.isConfirmed()) {
            return ValidationResult.failure("Email already confirmed");
        }
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ValidationResult.failure("Confirmation link have expired");
        }
        return ValidationResult.success();
    }
}
