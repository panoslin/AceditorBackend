package org.example.aceditorbackend.repository;

import org.example.aceditorbackend.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    @Query("SELECT folder FROM Folder folder WHERE folder.user.id = :userId AND folder.parent IS NULL")
    List<Folder> findAllRootFoldersByUserId(@Param("userId") Long userId);
}