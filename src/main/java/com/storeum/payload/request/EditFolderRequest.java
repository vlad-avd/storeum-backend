package com.storeum.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class EditFolderRequest {

    private String title;
    private Long parentFolderId;
}
