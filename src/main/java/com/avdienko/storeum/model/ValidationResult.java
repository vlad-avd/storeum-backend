package com.avdienko.storeum.model;

import lombok.Getter;

import static com.avdienko.storeum.model.ValidationStatus.FAIL;
import static com.avdienko.storeum.model.ValidationStatus.SUCCESS;

@Getter
public class ValidationResult {

    private ValidationStatus validationStatus;
    private String errorMessage;

    public ValidationResult(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public ValidationResult(ValidationStatus validationStatus, String errorMessage) {
        this.validationStatus = validationStatus;
        this.errorMessage = errorMessage;
    }

    public static ValidationResult success() {
        return new ValidationResult(SUCCESS);
    }

    public static ValidationResult failure(String errorMessage) {
        return new ValidationResult(FAIL, errorMessage);
    }
}
