package com.storeum.payload.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GenericResponse<T> {

    private T body;
    private HttpStatus status;
    private String errorMessage;

    public GenericResponse(T body, HttpStatus status) {
        this.body = body;
        this.status = status;
    }

    public GenericResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResponseEntity<?> buildResponseEntity() {
        if (isBadRequest()) {
            return ResponseEntity.badRequest().body(getErrorMessage());
        } else {
            return ResponseEntity.status(status).body(body);
        }
    }

    private boolean isBadRequest() {
        return body == null;
    }
}
