package com.avdienko.storeum.payload.request;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class CreateFolderRequest {
    @NotBlank
    private String title;
    @NotNull
    private Long userId;
}
