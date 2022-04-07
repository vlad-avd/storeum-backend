package com.storeum.payload.request;

import lombok.Getter;

@Getter
public class CreateNoteRequest {

    private String title;
    private String description;
    private String link;
}
