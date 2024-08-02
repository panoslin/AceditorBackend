package org.example.aceditorbackend.repository;

import org.example.aceditorbackend.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}