package com.avdienko.storeum.controller;

import com.avdienko.storeum.model.Folder;
import com.avdienko.storeum.model.User;
import com.avdienko.storeum.payload.request.CreateFolderRequest;
import com.avdienko.storeum.payload.response.MessageResponse;
import com.avdienko.storeum.repository.FolderRepository;
import com.avdienko.storeum.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/folders")
public class FolderController {
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createFolder(@Valid @RequestBody CreateFolderRequest createFolderRequest ) {
        User user = userRepository.findById(createFolderRequest.getUserId()).get();
        Folder folder = folderRepository.findById(5L).get();
        folder.setParentFolder(folderRepository.findById(3L).get());
        folderRepository.save(folder);
        folder.getSubFolders().add(folder);
        folderRepository.save(folder);
        return ResponseEntity.ok(new MessageResponse("Folder was created successfully."));
    }

    @GetMapping("/fcuk")
    public List<Folder> get(@RequestParam String userId) {
        List<Folder> folders = userRepository.findById(Long.valueOf(userId)).get().getFolders();
//        folders.forEach(System.out::println);
        return folders;
    }

    @GetMapping("/{id}")
    public Folder getFolder(@PathVariable Long id) {
        Folder folders = folderRepository.findById(id).get();
//        folders.forEach(System.out::println);
        return folders;
    }
}
