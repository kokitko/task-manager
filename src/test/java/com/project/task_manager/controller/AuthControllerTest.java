package com.project.task_manager.controller;

import com.project.task_manager.dto.LoginRequest;
import com.project.task_manager.dto.RegisterRequest;
import com.project.task_manager.entity.Role;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.RoleRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthControllerTest {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtUtils jwtUtils;

    private Role role;
    private UserEntity user;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    public void init() {
        role = new Role();
        role.setAuthority("ROLE_USER");

        user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("testemail");
        user.setPassword("testpassword");
        user.setRole(role);

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("testemail");
        registerRequest.setPassword("testpassword");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("testpassword");
    }
}
