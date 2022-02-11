package com.avdienko.storeum.controller;

import com.avdienko.storeum.model.ValidationResult;
import com.avdienko.storeum.model.entity.*;
import com.avdienko.storeum.payload.request.CreateNoteRequest;
import com.avdienko.storeum.repository.*;
import com.avdienko.storeum.validator.NoteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.avdienko.storeum.model.ValidationStatus.SUCCESS;
import static com.avdienko.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final NoteValidator noteValidator;

    @GetMapping("/users/{userId}/folders/{folderId}/notes")
    public List<Note> getFolderNotes(@PathVariable Long folderId) {
        return noteRepository.findNotesByFolderId(folderId);
    }

    @PostMapping("/users/{userId}/folders/{folderId}/notes")
    public ResponseEntity<?> createNote(@RequestBody CreateNoteRequest request,
                                        @PathVariable Long userId,
                                        @PathVariable Long folderId) {
        ValidationResult validationResult = noteValidator.validateCreateRequest(request);
        if (SUCCESS == validationResult.getValidationStatus()) {

            Note note = new Note();

            Optional<User> user = userRepository.findById(userId);
            user.ifPresentOrElse(note::setUser, () -> {throw new RuntimeException("User not found");});

            Optional<Folder> folder = folderRepository.findById(folderId);
            folder.ifPresentOrElse(note::setFolder, () -> {throw new RuntimeException("Folder not found");});

            note.setTitle(request.getTitle());
            note.setDescription(request.getDescription());
            note.setLink(request.getLink());

            return ResponseEntity.ok(noteRepository.save(note));
        } else {
            return ResponseEntity.badRequest().body(validationResult.getErrorMessage());
        }
    }
}
