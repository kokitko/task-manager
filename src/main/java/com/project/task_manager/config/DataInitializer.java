package com.project.task_manager.config;

import com.project.task_manager.entity.Role;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.RoleRepository;
import com.project.task_manager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initRolesAndAdmin(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            if (!roleRepository.findByAuthority("ROLE_USER").isPresent()) {
                Role userRole = new Role();
                userRole.setAuthority("ROLE_USER");
                roleRepository.save(userRole);
            }
            if (!roleRepository.findByAuthority("ROLE_ADMIN").isPresent()) {
                Role adminRole = new Role();
                adminRole.setAuthority("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            if (!userRepository.existsByUsername("admin")) {
                Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN")
                        .orElseThrow(() -> new RuntimeException("Role not found"));
                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setEmail("admin");
                admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        };
    }
}
