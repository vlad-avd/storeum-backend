package com.storeum.payload.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
public class EditProfileRequest {

    @Size(min = 3, max = 20)
    private String firstName;

    @Size(max = 50)
    @Email
    private String email;
}
