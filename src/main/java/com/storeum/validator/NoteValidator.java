package com.storeum.validator;

import com.storeum.model.ValidationResult;
import com.storeum.payload.request.CreateNoteRequest;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class NoteValidator {

    private final static String REQUIRED_FIELDS_MISSING_ERROR_MESSAGE =
            "At least one required field must be present: title or link";

    public ValidationResult validateCreateRequest(CreateNoteRequest createNoteRequest) {
        return isNotBlank(createNoteRequest.getTitle()) || isNotBlank(createNoteRequest.getLink())
                ? ValidationResult.success()
                : ValidationResult.failure(REQUIRED_FIELDS_MISSING_ERROR_MESSAGE);
    }
}
