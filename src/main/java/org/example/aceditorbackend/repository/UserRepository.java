package org.example.aceditorbackend.repository;

import org.example.aceditorbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}