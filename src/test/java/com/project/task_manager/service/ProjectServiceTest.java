package com.project.task_manager.service;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.service.impl.ProjectServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @InjectMocks
    private ProjectServiceImpl projectService;

    private UserEntity user = new UserEntity();
    private ProjectRequestDto projectRequestDto = new ProjectRequestDto();
    private Project project = new Project();

    @BeforeEach
    public void init() {
        user.setUsername("testuser");
        user.setEmail("testemail");
        user.setPassword("testpassword");

        projectRequestDto.setName("testprojectdto");
        projectRequestDto.setDescription("testdescriptionto");

        project.setName("testproject");
        project.setDescription("testdescription");
        project.setUser(user);
    }

    @Test
    public void ProjectService_CreateProject_ReturnsProjectResponseDto() {
        when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        ProjectResponseDto projectResponseDto = projectService.createProject(projectRequestDto, user);
        Assertions.assertThat(projectResponseDto.getName()).isEqualTo(project.getName());
    }
}
