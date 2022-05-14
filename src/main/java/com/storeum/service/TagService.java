package com.storeum.service;

import com.storeum.model.entity.Folder;
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

    public List<Tag> getExistingFolderTags(List<String> titles, Long folderId, Long userId) {
        return repository.findByTitleInAndFolderIdAndUserId(titles, folderId, userId);
    }

    public List<Tag> createTags(List<String> titles, Folder folder) {
        List<Tag> tags = titles.stream()
                .distinct()
                .map(it -> buildTag(it, folder))
                .toList();
        List<Tag> createdTags = repository.saveAll(tags);
        List<String> tagTitles = createdTags.stream()
                .map(Tag::getTitle)
                .toList();
        log.info("Tags with titles=[{}] were created", String.join(", ", tagTitles));
        return  createdTags;
    }

    private Tag buildTag(String title, Folder folder) {
        return Tag.builder()
                .title(title)
                .user(folder.getUser())
                .folder(folder)
                .build();
    }
}
