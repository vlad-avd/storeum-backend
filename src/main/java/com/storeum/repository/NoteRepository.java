package com.storeum.repository;

import com.storeum.model.entity.Note;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findNotesByFolderIdAndUserId(Long folderId, Long userId, Pageable page);
    
    Optional<Note> findByIdAndFolderIdAndUserId(Long noteId, Long folderId, Long userId);

    void deleteByIdAndFolderIdAndUserId(Long noteId, Long folderId, Long userId);
}
