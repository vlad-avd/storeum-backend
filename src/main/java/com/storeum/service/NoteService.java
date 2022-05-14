package com.storeum.service;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.ValidationResult;
import com.storeum.model.entity.*;
import com.storeum.payload.request.CreateNoteRequest;
import com.storeum.payload.request.EditNoteRequest;
import com.storeum.payload.response.GenericResponse;
import com.storeum.repository.NoteRepository;
import com.storeum.validator.NoteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static com.storeum.model.ValidationStatus.FAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final FolderService folderService;
    private final NoteValidator validator;
    private final UrlTitleExtractor titleExtractor;
    private final TagService tagService;

    public Note getNote(Long noteId, Long folderId, Long userId) {
        log.info("Trying to get note, id={}", noteId);
        return noteRepository.findByIdAndFolderIdAndUserId(noteId, folderId, userId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Note with id=%s was not found in DB", noteId))
        );
    }

    public List<Note> getFolderNotes(Long folderId, Long userId, Integer pageNumber) {
        if (pageNumber != null) {
            log.info("Trying to get folder notes, folderId={}, page={}", folderId, pageNumber);
            Pageable page = PageRequest.of(pageNumber, 12, Sort.by("createdAt").descending());
            return noteRepository.findNotesByFolderIdAndUserId(folderId, userId, page);
        } else {
            log.info("Trying to get folder notes, folderId={}", folderId);
            return noteRepository.findNotesByFolderIdAndUserId(folderId, userId);
        }
    }

    @Transactional
    public GenericResponse<Note> createNote(CreateNoteRequest request, Long userId, Long folderId) {
        log.info("Create note request received, title={}, description={}, link={}",
                request.getTitle(),
                request.getDescription(),
                request.getLink());

        ValidationResult validationResult = validator.validateCreateRequest(request);
        if (FAIL == validationResult.getValidationStatus()) {
            log.info("Create note request validation failed, cause={}", validationResult.getErrorMessage());
            return new GenericResponse<>(validationResult.getErrorMessage());
        }

        String noteTitle = Strings.isNotBlank(request.getTitle())
                ? request.getTitle()
                : titleExtractor.extract(request.getLink());

        Folder folder = folderService.getFolder(folderId, userId);
        User user = userService.getUserById(userId);

        List<Tag> tags = tagService.createTags(request.getTags(), folder);
        Note note = Note.builder()
                .title(noteTitle)
                .tags(new HashSet<>(tags))
                .description(request.getDescription())
                .link(request.getLink())
                .folder(folder)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Note createdNote = noteRepository.save(note);

        log.info("Note with id={} was created", createdNote.getId());

        return new GenericResponse<>(createdNote, HttpStatus.CREATED);
    }

    @Transactional
    public Note editNote(EditNoteRequest request, Long noteId, Long folderId, Long userId) {
        log.info("Edit note request received, noteId={}, title={}", noteId, request.getTitle());
        Note note = getNote(noteId, folderId, userId);
        note.setTitle(request.getTitle());
        note.setDescription(request.getDescription());
        note.setLink(request.getLink());
        note.setUpdatedAt(LocalDateTime.now());

        // clean up tags to remove (value from request) from current note tags array
        List<Tag> tagsAfterRemoving = note.getTags().stream()
                .filter(tag -> !request.getTagIdsToRemove().contains(tag.getId()))
                .toList();

        // prevent creation tags with given titles that already exists in DB
        List<Tag> alreadyExistedTags = tagService.getExistingFolderTags(request.getTagTitlesToCreate(),
                note.getFolder().getId(), note.getUser().getId());

        List<String> tagsToCreate = request.getTagTitlesToCreate().stream()
                .filter(title -> !alreadyExistedTags.stream().map(Tag::getTitle).toList().contains(title))
                .toList();
        List<Tag> newTags = tagService.createTags(tagsToCreate, note.getFolder());

        note.getTags().clear();
        note.getTags().addAll(tagsAfterRemoving);
        note.getTags().addAll(alreadyExistedTags);
        note.getTags().addAll(newTags);

        Note editedNote = noteRepository.save(note);
        log.info("Note was successfully edited, note={}", editedNote);

        return editedNote;
    }

    @Transactional
    public String deleteNote(Long noteId, Long folderId, Long userId) {
        log.info("Trying to delete note, id={}", noteId);
        noteRepository.deleteByIdAndFolderIdAndUserId(noteId, folderId, userId);
        return String.format("Note successfully deleted, id=%s", noteId);
    }
}
