package com.project.task_manager.service;

import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.impl.UserDetailsServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private UserEntity userEntity = new UserEntity(1L, "username",
                                            "password", "email", null);

    @Test
    public void UserDetailsService_LoadUserByUsername_ReturnsUserDetails() {
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(userEntity));

        User user = (User) userDetailsService.loadUserByUsername("username");
        Assertions.assertThat(user.getUsername()).isEqualTo(userEntity.getUsername());
    }
}
