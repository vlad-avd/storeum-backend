package com.storeum.payload.request;

import lombok.Getter;

@Getter
public class RegisterRequest {

    private String firstName;
    private String email;
    private String password;
    private String passwordConfirm;
}