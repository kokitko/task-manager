package com.project.task_manager.config;

import com.project.task_manager.entity.Role;
import com.project.task_manager.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
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
        };
    }
}
