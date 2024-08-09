package org.example.aceditorbackend.controller;

import org.example.aceditorbackend.config.CustomUserDetails;
import org.example.aceditorbackend.model.Folder;
import org.example.aceditorbackend.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/folders")
public class FolderController {
    @Autowired
    private FolderRepository folderRepository;

    @GetMapping
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Folder> getFolderById(@PathVariable Long id) {
        Optional<Folder> folder = folderRepository.findById(id);
        return folder.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Folder createFolder(@RequestBody Folder folder) {
        return folderRepository.save(folder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long id, @RequestBody Folder folderDetails) {
        return folderRepository.findById(id).map(folder -> {
            folder.setName(folderDetails.getName());
            folder.setParent(folderDetails.getParent());
            folder.setUser(folderDetails.getUser());
            folder.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(folderRepository.save(folder));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable Long id) {
        return folderRepository.findById(id).map(folder -> {
            folderRepository.delete(folder);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/root")
    public ResponseEntity<List<Folder>> getAllRootFoldersForAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();

        List<Folder> folders = folderRepository.findAllRootFoldersByUserId(userId);
        return ResponseEntity.ok(folders);
    }
}