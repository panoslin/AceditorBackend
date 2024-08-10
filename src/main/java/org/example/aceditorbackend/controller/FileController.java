package org.example.aceditorbackend.controller;

import org.example.aceditorbackend.model.File;
import org.example.aceditorbackend.repository.FileRepository;
import org.example.aceditorbackend.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileRepository fileRepository;

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
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
    public File createFile(@RequestBody File file) {
        return fileRepository.save(file);
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
    public ResponseEntity<List<File>> getAllRootFilesByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        List<File> files = fileRepository.findAllRootFilesByUserId(userId);
        return ResponseEntity.ok(files);
    }
}