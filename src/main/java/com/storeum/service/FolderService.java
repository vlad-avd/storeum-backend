package com.storeum.service;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.entity.Folder;
import com.storeum.model.entity.Tag;
import com.storeum.payload.request.CreateFolderRequest;
import com.storeum.payload.request.EditFolderRequest;
import com.storeum.payload.response.FolderResponse;
import com.storeum.payload.response.TagResponse;
import com.storeum.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserService userService;

    public Folder getFolder(Long folderId, Long userId) {
        log.info("Trying to get folder, id={}", folderId);
        return folderRepository.findByIdAndUserId(folderId, userId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Folder with id=%s was not found in DB", folderId))
        );
    }

    public List<Folder> getUserFolders(Long userId) {
        log.info("Trying to get user folders");
        return folderRepository.findByUserIdAndParentFolderIsNull(userId);
    }

    public Folder createFolder(CreateFolderRequest request, Long userId) {
        log.info("Create folder request received, title={}, parentFolderId={}",
                request.getTitle(),
                request.getParentFolderId());

        Folder folder = new Folder();
        if (request.getParentFolderId() != null) {
            Folder parentFolder = getFolder(request.getParentFolderId(), userId);
            folder.setParentFolder(parentFolder);
        }
        folder.setUser(userService.getUserById(userId));
        folder.setTitle(request.getTitle());

        Folder createdFolder = folderRepository.save(folder);
        //TODO: toString tags is null
        log.info("Folder was successfully created, folder={}", folder);
        return createdFolder;
    }

    public FolderResponse editFolder(EditFolderRequest request, Long folderId, Long userId) {
        log.info("Edit folder request received, folderId={}, title={}, parentFolderId={}",
                folderId,
                request.getTitle(),
                request.getParentFolderId());

        Folder folder = getFolder(folderId, userId);

        Folder parentFolder = request.getParentFolderId() != null
                ? getFolder(request.getParentFolderId(), userId)
                : null;

        folder.setTitle(request.getTitle());
        folder.setParentFolder(parentFolder);

        Folder editedFolder = folderRepository.save(folder);
        FolderResponse folderResponse = buildFolderResponse(editedFolder);
        log.info("Folder was successfully edited, folder={}", editedFolder);
        return folderResponse;
    }

    @Transactional
    public String deleteFolder(Long folderId, Long userId) {
        log.info("Trying to delete folder, id={}", folderId);
        folderRepository.deleteByIdAndUserId(folderId, userId);
        log.info("Folder successfully deleted, id={}", folderId);
        return String.format("Folder successfully deleted, id=%s", folderId);
    }

    private FolderResponse buildFolderResponse(Folder folder) {

        List<FolderResponse> subFolders = folder.getSubFolders() != null
                ? buildSubFolders(folder.getSubFolders())
                : new ArrayList<>();

        return FolderResponse.builder()
                .id(folder.getId())
                .title(folder.getTitle())
                .parentFolderId(folder.getParentFolder().getId())
                .tags(buildTags(folder.getTags()))
                .subFolders(subFolders)
                .build();
    }

    private List<FolderResponse> buildSubFolders(Set<Folder> folders) {
        return folders.stream()
                .map(this::buildFolderResponse)
                .toList();
    }

    private List<TagResponse> buildTags(Set<Tag> tags) {
        return tags.stream()
                .map(TagResponse::new)
                .toList();
    }
}
