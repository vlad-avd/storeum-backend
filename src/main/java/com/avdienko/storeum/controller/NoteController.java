package com.avdienko.storeum.controller;

import com.avdienko.storeum.model.entity.Note;
import com.avdienko.storeum.payload.request.CreateNoteRequest;
import com.avdienko.storeum.payload.request.EditNoteRequest;
import com.avdienko.storeum.payload.response.GenericResponse;
import com.avdienko.storeum.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.avdienko.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/users/{userId}/folders/{folderId}/notes")
    public List<Note> getFolderNotes(@PathVariable Long folderId) {
        return noteService.getFolderNotes(folderId);
    }

    @PostMapping("/users/{userId}/folders/{folderId}/notes")
    public ResponseEntity<String> createNote(@RequestBody CreateNoteRequest request,
                                             @PathVariable Long userId,
                                             @PathVariable Long folderId) {
        GenericResponse<Note> response = noteService.createNote(request, userId, folderId);
        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getResponseBody());
    }

    @PostMapping("/users/{userId}/folders/{folderId}/notes/{noteId}")
    public ResponseEntity<Note> editNote(@RequestBody EditNoteRequest request,
                                         @PathVariable Long noteId) {
        return ResponseEntity.ok(noteService.editNote(request, noteId));
    }

    @DeleteMapping("/users/{userId}/folders/{folderId}/notes/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable Long noteId) {
        return ResponseEntity.ok(noteService.deleteNote(noteId));
    }
}
