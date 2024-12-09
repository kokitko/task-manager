package com.project.task_manager.repository;

import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    UserEntity user = new UserEntity();
    Project project1 = new Project();
    Project project2 = new Project();

    @BeforeEach
    public void init() {
        user.setUsername("testusername");
        user.setEmail("testemail");
        user.setPassword("testpassword");
        userRepository.save(user);

        project1.setName("testproject1");
        project1.setDescription("testdescription1");
        project1.setUser(user);

        project2.setName("testproject2");
        project2.setDescription("testdescription2");
        project2.setUser(user);
    }

    @Test
    public void ProjectRepository_SaveAllProjects_ReturnsAllProjects() {
        List<Project> list = projectRepository.saveAll(List.of(project1, project2));

        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void ProjectRepository_SaveProject_ReturnsProject() {
        Project project = projectRepository.save(project1);

        Assertions.assertThat(project).isNotNull();
        Assertions.assertThat(project.getId()).isNotNull();
    }

    @Test
    public void ProjectRepository_FindByUser_ReturnsUserProjects() {
        projectRepository.saveAll(List.of(project1, project2));

        List<Project> list = projectRepository.findByUser(user);

        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void ProjectRepository_FindById_ReturnsProject() {
        Project savedProject = projectRepository.save(project1);

        Project project = projectRepository.findById(savedProject.getId()).orElseThrow(
                () -> new IllegalArgumentException("Project not found")
        );

        Assertions.assertThat(project).isNotNull();
        Assertions.assertThat(project.getId()).isEqualTo(project1.getId());
    }
}
