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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
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

        project.setId(1L);
        project.setName("testproject");
        project.setDescription("testdescription");
        project.setUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void ProjectService_CreateProject_ReturnsProjectResponseDto() {
        when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        ProjectResponseDto projectResponseDto = projectService.createProject(projectRequestDto, user);
        Assertions.assertThat(projectResponseDto.getName()).isEqualTo(project.getName());
    }

    @Test
    public void ProjectService_GetProjectsByUser_ReturnsProjectResponseDtoList() {
        when(projectRepository.findByUser(Mockito.any(UserEntity.class))).thenReturn(List.of(project));

        List<ProjectResponseDto> list = projectService.getProjectsByUser(user);
        Assertions.assertThat(list.get(0).getName()).isEqualTo(project.getName());
    }

    @Test
    public void ProjectService_UpdateProject_ReturnsProjectResponseDto() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
        when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        ProjectResponseDto response = projectService.updateProject(project.getId(), projectRequestDto);
        Assertions.assertThat(response.getName()).isEqualTo(projectRequestDto.getName());
    }
}
