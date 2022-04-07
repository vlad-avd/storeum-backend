package com.avdienko.storeum.validator;

import com.avdienko.storeum.model.ValidationResult;
import com.avdienko.storeum.payload.request.RegisterRequest;
import com.avdienko.storeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public ValidationResult validateRegisterRequest(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ValidationResult.failure("Error: Email is already in use.");
        }

        return ValidationResult.success();
    }

}
