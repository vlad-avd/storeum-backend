package com.avdienko.storeum.advice;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
public class ErrorResponse {
    private int statusCode;
    private LocalDate timestamp;
    private String message;
    private String description;
}