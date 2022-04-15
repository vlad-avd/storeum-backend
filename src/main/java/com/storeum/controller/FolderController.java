package com.storeum.controller;

import com.storeum.model.entity.Folder;
import com.storeum.payload.request.CreateFolderRequest;
import com.storeum.payload.request.EditFolderRequest;
import com.storeum.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.storeum.util.Constants.BASE_URL;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @GetMapping("/users/{userId}/folders")
    public List<Folder> getUserFolders(@PathVariable Long userId) {
        MDC.put("userId", String.valueOf(userId));
        return folderService.getUserFolders(userId);
    }

    @PostMapping("/users/{userId}/folders")
    public ResponseEntity<Folder> createFolder(@RequestBody CreateFolderRequest request,
                                               @PathVariable Long userId) {
        MDC.put("userId", String.valueOf(userId));
        Folder folder = folderService.createFolder(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(folder);
    }

    @PostMapping("/users/{userId}/folders/{folderId}")
    public ResponseEntity<Folder> editFolder(@RequestBody EditFolderRequest request,
                                             @PathVariable Long userId,
                                             @PathVariable Long folderId) {
        MDC.put("userId", String.valueOf(userId));
        Folder folder = folderService.editFolder(request, folderId);
        return ResponseEntity.ok(folder);
    }

    @DeleteMapping("/users/{userId}/folders/{folderId}")
    public ResponseEntity<String> deleteFolder(@PathVariable Long userId, @PathVariable Long folderId) {
        MDC.put("userId", String.valueOf(userId));
        String response = folderService.deleteFolder(folderId);
        return ResponseEntity.ok(response);
    }
}
