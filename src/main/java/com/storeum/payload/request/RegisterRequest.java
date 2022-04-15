package com.storeum.payload.request;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class RegisterRequest {

    private String firstName;
    private String email;
    private String password;
    //TODO: add password confirm field
}