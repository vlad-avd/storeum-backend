package com.avdienko.storeum.validator;

import com.avdienko.storeum.model.ValidationResult;
import com.avdienko.storeum.payload.request.RegisterRequest;
import com.avdienko.storeum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.avdienko.storeum.model.ValidationStatus.FAIL;
import static com.avdienko.storeum.model.ValidationStatus.SUCCESS;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public ValidationResult validateRegisterRequest(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ValidationResult.builder()
                    .validationStatus(FAIL)
                    .errorMessage("Error: Username is already taken.")
                    .build();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ValidationResult.builder()
                    .validationStatus(FAIL)
                    .errorMessage("Error: Email is already in use.")
                    .build();
        }

        return ValidationResult.builder()
                .validationStatus(SUCCESS)
                .build();
    }

}
