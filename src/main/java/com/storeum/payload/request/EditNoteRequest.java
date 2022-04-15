package com.storeum.payload.request;

import lombok.Getter;

import java.util.List;

@Getter
public class EditNoteRequest {

    private String title;
    private String description;
    private String link;
    private List<Long> tagIdsToRemove;
    private List<String> tagTitlesToCreate;
}
