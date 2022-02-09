package com.avdienko.storeum.controller;

import com.avdienko.storeum.model.Note;
import com.avdienko.storeum.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.avdienko.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;

    @GetMapping("/users/{userId}/folders/{folderId}/notes")
    public List<Note> getFolderNotes(@PathVariable Long folderId) {
        return noteRepository.findNotesByFolderId(folderId);
    }
}
