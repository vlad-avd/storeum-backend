package com.storeum.service;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.ValidationResult;
import com.storeum.model.entity.Note;
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

import java.time.LocalDateTime;
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

    public Note getNoteById(Long id) {
        log.info("Trying to get note, id={}", id);
        return noteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Note with id=%s was not found in DB", id))
        );
    }

    public List<Note> getFolderNotes(Long folderId, Integer pageNumber) {
        log.info("Trying to get folder notes, folderId={}", folderId);
        Pageable page = PageRequest.of(pageNumber, 12, Sort.by("createdAt").descending());
        return noteRepository.findNotesByFolderId(folderId, page);
    }

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

        Note note = Note.builder()
                .title(noteTitle)
                .description(request.getDescription())
                .link(request.getLink())
                .folder(folderService.getFolder(folderId, userId))
                .user(userService.getUserById(userId))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Note createdNote = noteRepository.save(note);
        log.info("Note was created, note={} ", createdNote);
        return new GenericResponse<>(createdNote, HttpStatus.CREATED);
    }

    public Note editNote(EditNoteRequest request, Long noteId) {
        log.info("Edit note request received, noteId={}, title={}, description={}, link={}",
                noteId,
                request.getTitle(),
                request.getDescription(),
                request.getLink());
        Note note = getNoteById(noteId);
        note.setTitle(request.getTitle());
        note.setDescription(request.getDescription());
        note.setLink(request.getLink());
        note.setUpdatedAt(LocalDateTime.now());

        Note editedNote = noteRepository.save(note);
        log.info("Note was successfully edited, note={}", editedNote);
        return note;
    }

    public String deleteNote(Long noteId) {
        log.info("Trying to delete note, id={}", noteId);
        noteRepository.deleteById(noteId);
        return String.format("Note successfully deleted, id=%s", noteId);
    }
}
