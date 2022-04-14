package com.storeum.controller;

import com.storeum.model.entity.Tag;
import com.storeum.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.storeum.util.Constants.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class TagController {

    private final TagService tagService;

    @PostMapping("/users/{userId}/folders/{folderId}/notes/{noteId}/tags")
    public Tag createTag(@RequestBody String title,
                         @PathVariable Long userId,
                         @PathVariable Long folderId,
                         @PathVariable Long noteId) {
        return tagService.createTag(title, userId, folderId, noteId);
    }
}
