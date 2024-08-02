package org.example.aceditorbackend.repository;

import org.example.aceditorbackend.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
}