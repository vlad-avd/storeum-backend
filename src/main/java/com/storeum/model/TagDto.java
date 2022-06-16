package com.storeum.model;

import com.storeum.model.entity.Tag;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class TagDto {

    private List<String> titlesToCreate;
    private List<Tag> alreadyExistingTags;
}
