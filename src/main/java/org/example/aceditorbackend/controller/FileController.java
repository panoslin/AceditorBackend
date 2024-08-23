package org.example.aceditorbackend.controller;

import org.example.aceditorbackend.model.File;
import org.example.aceditorbackend.model.User;
import org.example.aceditorbackend.repository.FileRepository;
import org.example.aceditorbackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileController(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) {
        Optional<File> file = fileRepository.findById(id);
        return file.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public File createFile(@AuthenticationPrincipal Jwt jwt, @RequestBody File file) {
        String email = jwt.getClaimAsString("email");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            file.setUser(user.get());
            return fileRepository.save(file);
        } else {
            // user not found
            return null;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<File> updateFile(@PathVariable Long id, @RequestBody File fileDetails) {
        return fileRepository.findById(id).map(file -> {
            file.setName(fileDetails.getName());
            file.setContent(fileDetails.getContent());
            file.setFolder(fileDetails.getFolder());
            file.setUser(fileDetails.getUser());
            file.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(fileRepository.save(file));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        return fileRepository.findById(id).map(file -> {
            fileRepository.delete(file);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/root")
    public ResponseEntity<List<File>> getAllRootFilesByUserId(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Long userId = user.get().getId();
            List<File> files = fileRepository.findAllRootFilesByUserId(userId);
            return ResponseEntity.ok(files);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}