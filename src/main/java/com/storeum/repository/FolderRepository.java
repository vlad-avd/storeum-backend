package com.storeum.repository;

import com.storeum.model.entity.Folder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @EntityGraph(value = "entireFolderTree")
    List<Folder> findByUserIdAndParentFolderIsNull(Long userId);

    @EntityGraph(value = "entireFolderTree")
    Optional<Folder> findByIdAndUserId(Long folderId, Long userId);

    void deleteByIdAndUserId(Long folderId, Long userId);
}
