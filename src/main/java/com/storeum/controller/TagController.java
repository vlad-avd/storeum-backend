package com.storeum.controller;

import com.storeum.model.entity.Tag;
import com.storeum.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/users/{userId}")
    public List<Tag> getUserTags(@PathVariable Long userId) {
        return tagService.getUserTags(userId);
    }

    @GetMapping("/users/{userId}/folders/{folderId}")
    public List<Tag> getFolderTags(@PathVariable Long userId, @PathVariable Long folderId) {
        return tagService.getFolderTags(userId, folderId);
    }

    @GetMapping("/users/{userId}/folders/{folderId}/notes/{noteId}")
    public List<Tag> getNoteTags(@PathVariable Long userId, @PathVariable Long folderId, @PathVariable Long noteId) {
        return tagService.getNoteTags(userId, folderId, noteId);
    }
}
