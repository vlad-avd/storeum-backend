package com.avdienko.storeum.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class EditFolderRequest {

    @NotBlank
    private String title;
    private Long parentFolderId;
}
