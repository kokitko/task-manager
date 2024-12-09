package com.project.task_manager.repository;

import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void ProjectRepository_SaveAllProjects_ReturnsAllProjects() {
        UserEntity user = UserEntity.builder()
                .username("testusername")
                .email("testemail")
                .password("testpassword")
                .build();

        userRepository.save(user);

        Project project1 = Project.builder()
                .name("testproject1")
                .description("testdescription1")
                .user(user)
                .build();

        Project project2 = Project.builder()
                .name("testproject2")
                .description("testdescription2")
                .user(user)
                .build();

        List<Project> list = projectRepository.saveAll(List.of(project1, project2));

        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(2);
    }
}
