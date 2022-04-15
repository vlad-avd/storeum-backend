package com.storeum.service;

import com.storeum.model.entity.Note;
import com.storeum.model.entity.Tag;
import com.storeum.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository repository;

    public void createTags(List<String> titles, Note note) {
        List<Tag> tags = titles.stream()
                .map(it -> buildTag(it, note))
                .toList();
        List<Tag> createdTags = repository.saveAll(tags);
        List<String> tagTitles = createdTags.stream()
                .map(Tag::getTitle)
                .toList();
        log.info("Tags with titles=[{}] were created", String.join(", ", tagTitles));
    }

    private Tag buildTag(String title, Note note) {
        return Tag.builder()
                .title(title)
                .user(note.getUser())
                .folder(note.getFolder())
                .note(note)
                .build();
    }
}
