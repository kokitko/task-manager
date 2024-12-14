package com.project.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.task_manager.dto.LoginRequest;
import com.project.task_manager.dto.RegisterRequest;
import com.project.task_manager.entity.Role;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.RoleRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtUtils jwtUtils;
    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    public void AuthController_RegisterUser_ReturnsString() throws Exception {
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("testpassword");
        when(roleRepository.findByAuthority(Mockito.anyString())).thenReturn(Optional.of(role));
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    public void AuthController_LoginUser_ReturnsString() throws Exception {
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(jwtUtils.generateToken(Mockito.anyString(), Mockito.any(Role.class))).thenReturn("testtoken");

        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("testtoken"));
    }
}
