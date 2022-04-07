package com.storeum.repository;

import com.storeum.model.entity.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findNotesByFolderId(Long id, Pageable page);
}
