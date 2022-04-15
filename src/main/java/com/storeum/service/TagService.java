package com.storeum.service;

import com.storeum.model.entity.Tag;
import com.storeum.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository repository;

    public Tag createTag(String title, Long userId, Long folderId, Long noteId) {
        Tag tag = Tag.builder()
                .title(title)
//                .user(userService.getUserById(userId))
//                .folder(folderService.getFolder(folderId, userId))
//                .note(noteService.getNoteById(noteId))
                .build();
        Tag createdTag = repository.save(tag);
        log.info("Tag with title={} was created", title);
        return createdTag;
    }

    public void deleteTag(Long id) {
        repository.deleteById(id);
        log.info("Tag with id={} was deleted", id);
    }
}
