package org.example.aceditorbackend.initializer;

import org.example.aceditorbackend.model.File;
import org.example.aceditorbackend.model.Folder;
import org.example.aceditorbackend.model.User;
import org.example.aceditorbackend.repository.FileRepository;
import org.example.aceditorbackend.repository.FolderRepository;
import org.example.aceditorbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, FolderRepository folderRepository, FileRepository fileRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create users
            User user1 = new User();
            user1.setUsername("admin");
            user1.setEmail("admin@example.com");
            user1.setPassword(passwordEncoder.encode("password"));

            User user2 = new User();
            user2.setUsername("panos");
            user2.setEmail("user@example.com");
            user2.setPassword(passwordEncoder.encode("password"));

            User user3 = new User();
            user3.setUsername("panos");
            user3.setEmail("lghpanos@gmail.com");
            user3.setPassword(passwordEncoder.encode("password"));

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            // Create folders for user1
            Folder rootFolder1 = new Folder();
            rootFolder1.setName("rootFolder1");
            rootFolder1.setUser(user1);

            Folder subFolder1 = new Folder();
            subFolder1.setName("subFolder1");
            subFolder1.setUser(user1);
            subFolder1.setParent(rootFolder1);

            rootFolder1.setSubfolders(List.of(subFolder1));

            folderRepository.save(rootFolder1);
            folderRepository.save(subFolder1);

            // Create files for user1
            File file1 = new File();
            file1.setName("file1.txt");
            file1.setContent("This is file 1");
            file1.setUser(user1);
            file1.setFolder(rootFolder1);

            File file2 = new File();
            file2.setName("file2.txt");
            file2.setContent("This is file 2");
            file2.setUser(user1);
            file2.setFolder(subFolder1);

            fileRepository.save(file1);
            fileRepository.save(file2);
        };
    }
}