package com.storeum.payload.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class EditNoteRequest {

    private String title;
    private String description;
    private String link;
    private Map<String, String> tagTitleAction;
}
