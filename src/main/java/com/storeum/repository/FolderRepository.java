package com.storeum.repository;

import com.storeum.model.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findByUserIdAndParentFolderIsNull(Long userId);
}
