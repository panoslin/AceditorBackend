package org.example.aceditorbackend.service;

import org.example.aceditorbackend.dto.UserRegistrationDTO;
import org.example.aceditorbackend.model.User;
import org.example.aceditorbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(UserRegistrationDTO userRegistrationDTO) {
//        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
//            throw new DataIntegrityViolationException("Email is already in use.");
//        }

        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setEmail(userRegistrationDTO.getEmail());
//        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        return userRepository.save(user);
    }

    public User createOrRetrieveUser(Map<String, Object> claims) {
        String email = (String) claims.get("email");
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername((String) claims.get("name"));
            // Set other user attributes as needed
            return userRepository.save(newUser);
        }
    }
}