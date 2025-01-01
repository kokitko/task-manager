package com.project.task_manager.repository;

import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    UserEntity user = new UserEntity();
    UserEntity user2 = new UserEntity();

    @BeforeEach
    public void init() {
        user.setUsername("testusername");
        user.setEmail("testemail");
        user.setPassword("testpassword");

        user2.setUsername("testusername2");
        user2.setEmail("testemail2");
        user2.setPassword("testpassword2");
    }

    @Test
    public void UserRepository_FindAllUsers_ReturnsAllUsers() {
        userRepository.save(user);
        userRepository.save(user2);
        Pageable pageable = Pageable.ofSize(2).withPage(0);

        Page<UserEntity> users = userRepository.findAll(pageable);

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void UserRepository_SaveUser_ReturnsUser() {
        UserEntity savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void UserRepository_FindUserByUsername_ReturnsUser() {
        userRepository.save(user);

        UserEntity foundUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void UserRepository_ExistsByUsername_ReturnsTrue() {
        userRepository.save(user);

        boolean exists = userRepository.existsByUsername(user.getUsername());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void UserRepository_ExistsByEmail_ReturnsTrue() {
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail(user.getEmail());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void UserRepository_DeleteById_ReturnsNull() {
        UserEntity savedUser = userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<UserEntity> deletedUser = userRepository.findById(savedUser.getId());
        Assertions.assertThat(deletedUser).isEmpty();
    }
}
