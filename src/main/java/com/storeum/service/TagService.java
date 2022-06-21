package com.storeum.service;

import com.storeum.model.TagDto;
import com.storeum.model.entity.Folder;
import com.storeum.model.entity.Tag;
import com.storeum.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository repository;

    public List<Tag> getUserTags(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Tag> getFolderTags(Long userId, Long folderId) {
        return repository.findByUserIdAndFolderId(userId, folderId);
    }

    public List<Tag> getNoteTags(Long userId, Long folderId, Long noteId) {
        return repository.findByUserIdAndFolderIdAndNoteId(userId, folderId, noteId);
    }

    public List<Tag> createTags(List<String> titles, Folder folder) {
        TagDto tagDto = groupTags(titles, folder.getId(), folder.getUser().getId());

        List<Tag> tags = tagDto.getTitlesToCreate().stream()
                .map(it -> buildTag(it, folder))
                .toList();

        List<Tag> createdTags = repository.saveAll(tags);
        List<String> tagTitles = createdTags.stream()
                .map(Tag::getTitle)
                .toList();

        log.info("Tags with titles=[{}] were created", String.join(", ", tagTitles));

        return new ArrayList<>() {{
            addAll(createdTags);
            addAll(tagDto.getAlreadyExistingTags());
        }};
    }

    public TagDto groupTags(List<String> titles, Long folderId, Long userId) {
        // prevent creation tags with given titles that already exists in DB
        List<Tag> alreadyExistingTags = repository.findByTitleInAndFolderIdAndUserId(titles, folderId, userId);
        List<String> titlesToCreate = titles.stream()
                .distinct()
                .filter(title -> !alreadyExistingTags.stream().map(Tag::getTitle).toList().contains(title))
                .toList();

        return new TagDto(titlesToCreate, alreadyExistingTags);
    }

    public void cleanUpDetachedTags(Long userId) {
        repository.deleteByUserIdAndNoteIsNull(userId);
    }

    private Tag buildTag(String title, Folder folder) {
        return Tag.builder()
                .title(title)
                .user(folder.getUser())
                .folder(folder)
                .build();
    }
}
