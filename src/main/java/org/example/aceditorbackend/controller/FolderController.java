package org.example.aceditorbackend.controller;

import org.example.aceditorbackend.model.Folder;
import org.example.aceditorbackend.model.User;
import org.example.aceditorbackend.repository.FolderRepository;
import org.example.aceditorbackend.repository.UserRepository;
import org.example.aceditorbackend.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public FolderController(
            FolderRepository folderRepository,
            UserRepository userRepository
    ) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

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
    public Folder createFolder(@AuthenticationPrincipal Jwt jwt, @RequestBody Folder folder) {
        String email = jwt.getClaimAsString("email");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            folder.setUser(user.get());
            return folderRepository.save(folder);
        } else {
            // user not found
            return null;
        }
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
    public ResponseEntity<List<Folder>> getAllRootFoldersForAuthenticatedUser(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Long userId = user.get().getId();
            List<Folder> folders = folderRepository.findAllRootFoldersByUserId(userId);
            return ResponseEntity.ok(folders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}