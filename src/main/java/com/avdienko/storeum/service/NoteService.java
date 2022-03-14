package com.avdienko.storeum.service;

import com.avdienko.storeum.exception.ResourceNotFoundException;
import com.avdienko.storeum.model.ValidationResult;
import com.avdienko.storeum.model.entity.Note;
import com.avdienko.storeum.payload.request.CreateNoteRequest;
import com.avdienko.storeum.payload.request.EditNoteRequest;
import com.avdienko.storeum.payload.response.GenericResponse;
import com.avdienko.storeum.repository.NoteRepository;
import com.avdienko.storeum.validator.NoteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.avdienko.storeum.model.ValidationStatus.FAIL;
import static com.avdienko.storeum.util.MessageFormatters.noteNotFound;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final FolderService folderService;
    private final NoteValidator validator;

    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(noteNotFound(id)));
    }

    public List<Note> getFolderNotes(Long folderId) {
        return noteRepository.findNotesByFolderId(folderId);
    }

    public GenericResponse<Note> createNote(CreateNoteRequest request, Long userId, Long folderId) {
        ValidationResult validationResult = validator.validateCreateRequest(request);
        if (FAIL == validationResult.getValidationStatus()) {
            return GenericResponse.<Note>builder()
                    .errorMessage(validationResult.getErrorMessage())
                    .statusCode(HttpStatus.BAD_REQUEST)
                    .build();
        }

        Note note = Note.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .link(request.getLink())
                .folder(folderService.getFolderById(folderId))
                .user(userService.getUserById(userId))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return GenericResponse.<Note>builder()
                .body(note)
                .statusCode(HttpStatus.OK)
                .build();
    }

    public Note editNote(EditNoteRequest request, Long noteId) {
        Note note = getNoteById(noteId);

        note.setTitle(request.getTitle());
        note.setDescription(request.getDescription());
        note.setLink(request.getLink());
        note.setUpdatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }

    public String deleteNote(Long noteId) {
        noteRepository.deleteById(noteId);
        return "Note successfully deleted, id=" + noteId;
    }
}
