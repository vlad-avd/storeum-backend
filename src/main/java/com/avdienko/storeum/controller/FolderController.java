package com.avdienko.storeum.controller;

import com.avdienko.storeum.model.Folder;
import com.avdienko.storeum.model.User;
import com.avdienko.storeum.payload.request.CreateFolderRequest;
import com.avdienko.storeum.payload.response.MessageResponse;
import com.avdienko.storeum.repository.FolderRepository;
import com.avdienko.storeum.repository.UserRepository;
import com.avdienko.storeum.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.avdienko.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class FolderController {

    private FolderRepository folderRepository;
    private UserRepository userRepository;

    @PostMapping("/users/{userId}/folders")
    public ResponseEntity<?> createFolder(@Valid @RequestBody CreateFolderRequest request,
                                          @PathVariable Long userId) {
        Folder folder = new Folder();
        if (request.getParentFolderId() != null) {
            Optional<Folder> parentFolder = folderRepository.findById(request.getParentFolderId());
            parentFolder.ifPresent(folder::setParentFolder);
        }

        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(folder::setUser);

        folder.setTitle(request.getTitle());
        folderRepository.save(folder);
        return ResponseEntity.ok(folder);
    }

    @GetMapping("/users/{userId}/folders")
    public List<Folder> getUserFolders(@PathVariable Long userId) {
        return folderRepository.findByUserIdAndParentFolderIsNull(userId);
    }

    @GetMapping("/users/{userId}/folders/{folderId}")
    public ResponseEntity<?> getFolder(@PathVariable Long folderId) {
        Optional<Folder> folder = folderRepository.findById(folderId);
        String notFoundBody = "Folder was not found, id=" + folderId;
        return HttpUtil.okOrNotFound(folder, notFoundBody);
    }

    @PutMapping("/users/{userId}/folders/{folderId}")
    public Folder editFolder(@PathVariable Long userId, @PathVariable Long folderId) {
        return new Folder();
    }

    @DeleteMapping("/users/{userId}/folders/{folderId}")
    public ResponseEntity<String> deleteFolder(@PathVariable Long userId, @PathVariable Long folderId) {
        folderRepository.deleteById(folderId);
        return ResponseEntity.ok("Folder successfully deleted, id=" + folderId);
    }
}
