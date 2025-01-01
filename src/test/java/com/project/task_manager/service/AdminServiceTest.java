package com.project.task_manager.service;

import com.project.task_manager.dto.*;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.service.impl.AdminServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private AdminServiceImpl adminService;

    private UserEntity user = new UserEntity();
    private ProjectRequestDto projectRequestDto = new ProjectRequestDto();
    private Project project = new Project();
    private TaskRequestDto taskRequestDto = new TaskRequestDto();
    private Task task = new Task();

    @BeforeEach
    public void init() {
        user.setUsername("testuser");
        user.setEmail("testemail");
        user.setPassword("testpassword");
        user.setId(1L);

        projectRequestDto.setName("testprojectdto");
        projectRequestDto.setDescription("testdescriptionto");

        project.setId(1L);
        project.setName("testproject");
        project.setDescription("testdescription");
        project.setUser(user);

        taskRequestDto.setName("testtaskdto");
        taskRequestDto.setDescription("testdescriptionto");

        task.setId(1L);
        task.setName("testtask");
        task.setDescription("testdescription");
        task.setProject(project);
    }

    @Test
    public void AdminService_CreateProject_ReturnsProjectResponseDto() {
        when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        ProjectResponseDto projectResponseDto = adminService.createProject(projectRequestDto, user);
        Assertions.assertThat(projectResponseDto.getName()).isEqualTo(project.getName());
    }

/*    @Test
    public void AdminService_GetProjectsByUser_ReturnsProjectResponseDtoList() {
        when(projectRepository.findByUser(Mockito.any(UserEntity.class))).thenReturn(List.of(project));

        List<ProjectResponseDto> projectResponseDtoList = adminService.getProjectsByUser(user);
        Assertions.assertThat(projectResponseDtoList).hasSize(1);
    }*/

    @Test
    public void AdminService_UpdateProject_ReturnsProjectResponseDto() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
        when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        ProjectResponseDto projectResponseDto = adminService.updateProject
                (project.getId(), projectRequestDto, user.getId());
        Assertions.assertThat(projectResponseDto.getName()).isEqualTo(projectRequestDto.getName());
    }

    @Test
    public void AdminService_DeleteProject_ReturnsVoid() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));

        assertAll(() -> adminService.deleteProject(project.getId(), user.getId()));
    }

    @Test
    public void AdminService_CreateTask_ReturnsTaskResponseDto() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto taskResponseDto = adminService.createTask(taskRequestDto, project.getId(), user.getId());
        Assertions.assertThat(taskResponseDto.getName()).isEqualTo(task.getName());
    }

    @Test
    public void AdminService_GetTasksByProject_ReturnsTaskResponsePage() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
        PageImpl<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findByProject(Mockito.any(Project.class), Mockito.any(Pageable.class)))
                .thenReturn(page);

        Pageable pageable = Pageable.ofSize(1).withPage(0);
        TaskResponsePage taskResponsePage = adminService
                .getTasksByProject(project.getId(), 1L, 0, 1);

        Assertions.assertThat(taskResponsePage.getTasks().get(0).getName()).isEqualTo(task.getName());
        Assertions.assertThat(taskResponsePage.getTotalPages()).isEqualTo(page.getTotalPages());
    }

    @Test
    public void AdminService_UpdateTask_ReturnsTaskResponseDto() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto taskResponseDto = adminService.updateTask
                (task.getId(), taskRequestDto, project.getId(), user.getId());
        Assertions.assertThat(taskResponseDto.getName()).isEqualTo(task.getName());
    }

    @Test
    public void AdminService_DeleteTask_ReturnsVoid() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task));

        assertAll(() -> adminService.deleteTask(task.getId(), project.getId(), user.getId()));
    }
}
