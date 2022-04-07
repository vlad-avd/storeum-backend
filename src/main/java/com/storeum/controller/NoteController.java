package com.storeum.controller;

import com.storeum.model.entity.Note;
import com.storeum.payload.request.CreateNoteRequest;
import com.storeum.payload.request.EditNoteRequest;
import com.storeum.payload.response.GenericResponse;
import com.storeum.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/users/{userId}/folders/{folderId}/notes/{noteId}")
    public ResponseEntity<Note> getNote(@PathVariable Long userId,
                                        @PathVariable Long noteId) {
        MDC.put("userId", String.valueOf(userId));
        return ResponseEntity.ok(noteService.getNoteById(noteId));
    }

    @GetMapping("/users/{userId}/folders/{folderId}/notes")
    public List<Note> getFolderNotes(@PathVariable Long userId,
                                     @PathVariable Long folderId,
                                     @RequestParam("page") Integer pageNumber) {
        MDC.put("userId", String.valueOf(userId));
        return noteService.getFolderNotes(folderId, pageNumber);
    }

    @PostMapping("/users/{userId}/folders/{folderId}/notes")
    public ResponseEntity<?> createNote(@RequestBody CreateNoteRequest request,
                                        @PathVariable Long userId,
                                        @PathVariable Long folderId) {
        MDC.put("userId", String.valueOf(userId));
        GenericResponse<Note> response = noteService.createNote(request, userId, folderId);
        return response.buildResponseEntity();
    }

    @PostMapping("/users/{userId}/folders/{folderId}/notes/{noteId}")
    public ResponseEntity<Note> editNote(@RequestBody EditNoteRequest request,
                                         @PathVariable Long userId,
                                         @PathVariable Long noteId) {
        MDC.put("userId", String.valueOf(userId));
        return ResponseEntity.ok(noteService.editNote(request, noteId));
    }

    @DeleteMapping("/users/{userId}/folders/{folderId}/notes/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable Long userId,
                                             @PathVariable Long noteId) {
        MDC.put("userId", String.valueOf(userId));
        return ResponseEntity.ok(noteService.deleteNote(noteId));
    }
}
