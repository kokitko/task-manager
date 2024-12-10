package com.project.task_manager.repository;

import com.project.task_manager.entity.Role;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;

    Role user = new Role();
    Role admin = new Role();

    @BeforeEach
    public void init() {
        user.setAuthority("ROLE_USER");
        admin.setAuthority("ROLE_ADMIN");
    }

    @Test
    public void RoleRepository_SaveRole_ReturnsRole() {
        Role savedRole = roleRepository.save(user);

        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getAuthority()).isEqualTo(user.getAuthority());
    }

    @Test
    public void RoleRepository_FindByAuthority_ReturnsRole() {
        roleRepository.save(user);

        Role foundRole = roleRepository.findByAuthority(user.getAuthority())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Assertions.assertThat(foundRole).isNotNull();
        Assertions.assertThat(foundRole.getAuthority()).isEqualTo(user.getAuthority());
    }
}
