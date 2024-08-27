package org.example.aceditorbackend.controller;

import jakarta.validation.Valid;
import org.example.aceditorbackend.dto.UserRegistrationDTO;
import org.example.aceditorbackend.model.User;
import org.example.aceditorbackend.repository.UserRepository;
import org.example.aceditorbackend.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(
            UserService userService,
            UserRepository userRepository
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            String email = jwt.getClaimAsString("email");
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                // user already exists
                return ResponseEntity.ok(user.get());
            } else {
                User registeredUser = userService.registerNewUser(userRegistrationDTO);
                return ResponseEntity.ok(registeredUser);
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}