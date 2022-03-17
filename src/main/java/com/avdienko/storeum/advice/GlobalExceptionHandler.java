package com.avdienko.storeum.advice;

import com.avdienko.storeum.exception.ResourceNotFoundException;
import com.avdienko.storeum.exception.TokenRefreshException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        log.error("Error while refreshing token, ex={}", ex.getMessage());
        ErrorResponse response = buildErrorResponse(ex,HttpStatus.FORBIDDEN, request);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException ex, WebRequest request){
        log.error("Error while retrieving resource from DB, ex={}", ex.getMessage());
        ErrorResponse response = buildErrorResponse(ex,HttpStatus.NOT_FOUND, request);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(UsernameNotFoundException ex, WebRequest request){
        log.error("Error while retrieving resource from DB, ex={}", ex.getMessage());
        ErrorResponse response = buildErrorResponse(ex,HttpStatus.NOT_FOUND, request);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex, WebRequest request){
        log.error("Unknown exception, ex: ", ex);
        ErrorResponse response = buildErrorResponse(ex,HttpStatus.INTERNAL_SERVER_ERROR, request);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    private ErrorResponse buildErrorResponse(Exception ex, HttpStatus httpStatus, WebRequest request) {
        return ErrorResponse.builder()
                .statusCode(httpStatus.value())
                .timestamp(LocalDate.now())
                .message(ex.getMessage())
                .description(request.getDescription(false))
                .build();
    }
}
