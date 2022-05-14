package com.storeum.payload.response;

import com.storeum.model.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class TagResponse {

    private Long id;
    private String title;

    public TagResponse(Tag tag) {
        this.id = tag.getId();
        this.title = tag.getTitle();
    }
}
