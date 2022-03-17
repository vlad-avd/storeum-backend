package com.avdienko.storeum.service;

import com.avdienko.storeum.exception.ResourceNotFoundException;
import com.avdienko.storeum.model.entity.Folder;
import com.avdienko.storeum.payload.request.CreateFolderRequest;
import com.avdienko.storeum.payload.request.EditFolderRequest;
import com.avdienko.storeum.repository.FolderRepository;
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

    public Folder getFolderById(Long id) {
        log.info("Trying to get folder, id={}", id);
        return folderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Folder with id=%s was not found in DB", id))
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
            folder.setParentFolder(getFolderById(request.getParentFolderId()));
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
        Folder folder = getFolderById(folderId);
        Folder parentFolder = getFolderById(request.getParentFolderId());

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
