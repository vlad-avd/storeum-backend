package com.avdienko.storeum.payload.request;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}