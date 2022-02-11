package com.avdienko.storeum.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationResult {

    private ValidationStatus validationStatus;
    private String errorMessage;
}
