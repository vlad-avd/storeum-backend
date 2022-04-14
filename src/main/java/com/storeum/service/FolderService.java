package com.storeum.service;

import com.storeum.exception.ResourceNotFoundException;
import com.storeum.model.entity.Folder;
import com.storeum.payload.request.CreateFolderRequest;
import com.storeum.payload.request.EditFolderRequest;
import com.storeum.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
            folder.setParentFolder(getFolder(request.getParentFolderId(), userId));
        }
        folder.setUser(userService.getUserById(userId));
        folder.setTitle(request.getTitle());

        Folder createdFolder = folderRepository.save(folder);
        log.info("Folder was successfully created, folder={}", folder);
        return createdFolder;
    }

    public Folder editFolder(EditFolderRequest request, Long folderId) {
        log.info("Edit folder request received, folderId={}, title={}, parentFolderId={}",
                folderId,
                request.getTitle(),
                request.getParentFolderId());
        //TODO;
        Folder folder = getFolder(folderId, 1L);
        Folder parentFolder = getFolder(request.getParentFolderId(), 1L);

        folder.setTitle(request.getTitle());
        folder.setParentFolder(parentFolder);

        Folder editedFolder = folderRepository.save(folder);
        log.info("Folder was successfully edited, folder={}", editedFolder);
        return editedFolder;
    }

    public String deleteFolder(Long folderId) {
        log.info("Trying to delete folder, id={}", folderId);
        folderRepository.deleteById(folderId);
        log.info("Folder successfully deleted, id={}", folderId);
        return "Folder successfully deleted, id=" + folderId;
    }
}
