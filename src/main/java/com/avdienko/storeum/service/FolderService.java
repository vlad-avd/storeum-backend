package com.avdienko.storeum.service;

import com.avdienko.storeum.exception.ResourceNotFoundException;
import com.avdienko.storeum.model.entity.Folder;
import com.avdienko.storeum.payload.request.CreateFolderRequest;
import com.avdienko.storeum.payload.request.EditFolderRequest;
import com.avdienko.storeum.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.avdienko.storeum.util.MessageFormatters.folderNotFound;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserService userService;

    public Folder getFolderById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(folderNotFound(id)));
    }

    public List<Folder> getUserFolders(Long userId) {
        return folderRepository.findByUserIdAndParentFolderIsNull(userId);
    }

    public Folder createFolder(CreateFolderRequest request, Long userId) {
        Folder folder = new Folder();

        if (request.getParentFolderId() != null) {
            folder.setParentFolder(getFolderById(request.getParentFolderId()));
        }
        folder.setUser(userService.getUserById(userId));
        folder.setTitle(request.getTitle());

        return folderRepository.save(folder);
    }

    public Folder editFolder(EditFolderRequest request, Long folderId) {
        Folder folder = getFolderById(folderId);
        Folder parentFolder = getFolderById(request.getParentFolderId());

        folder.setTitle(request.getTitle());
        folder.setParentFolder(parentFolder);

        return folderRepository.save(folder);
    }

    public String deleteFolder(Long folderId) {
        folderRepository.deleteById(folderId);
        return "Folder successfully deleted, id=" + folderId;
    }
}
