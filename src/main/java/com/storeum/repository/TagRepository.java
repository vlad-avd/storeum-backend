package com.storeum.repository;

import com.storeum.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByTitleInAndFolderIdAndUserId(List<String> titles, Long folderId, Long userId);
}
