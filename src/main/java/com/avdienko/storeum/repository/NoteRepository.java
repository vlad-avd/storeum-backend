package com.avdienko.storeum.repository;

import com.avdienko.storeum.model.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findNotesByFolderId(Long id);
}
