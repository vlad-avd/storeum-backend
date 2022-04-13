package com.storeum.advice;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ErrorResponse {
    private int statusCode;
    private LocalDate timestamp;
    private String message;
    private String description;
}