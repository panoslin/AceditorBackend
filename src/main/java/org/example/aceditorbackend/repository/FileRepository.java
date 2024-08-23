package org.example.aceditorbackend.repository;

import org.example.aceditorbackend.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT file FROM File file WHERE file.user.id = :userId AND file.folder IS NULL")
    List<File> findAllRootFilesByUserId(@Param("userId") Long userId);
}