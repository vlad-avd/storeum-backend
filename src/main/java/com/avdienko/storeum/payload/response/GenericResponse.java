package com.avdienko.storeum.payload.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class GenericResponse<T> {

    private T body;
    private String errorMessage;

    public GenericResponse(T body) {
        this.body = body;
    }

    public GenericResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResponseEntity<?> resolveResponse() {
        if (isBadRequest()) {
            return ResponseEntity.badRequest().body(getErrorMessage());
        } else {
            return ResponseEntity.ok(getBody());
        }
    }

    private boolean isBadRequest() {
        return body == null;
    }
}
