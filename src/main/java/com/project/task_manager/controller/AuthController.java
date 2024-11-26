package com.project.task_manager.controller;

import com.project.task_manager.dto.LoginRequest;
import com.project.task_manager.dto.RegisterRequest;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.UserNotFoundException;
import com.project.task_manager.repository.RoleRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtUtils jwtUtils, RoleRepository roleRepository,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail()) ||
        userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body
                    ("User with this email or username already exists");
        }
        UserEntity user = new UserEntity();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleRepository.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found")));  // not handling because roles are initialized so it is never going to be thrown
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(token);
    }
}
