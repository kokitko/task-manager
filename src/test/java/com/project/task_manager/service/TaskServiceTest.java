package com.project.task_manager.service;

import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.service.impl.TaskServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private TaskServiceImpl taskService;

    private UserEntity user = new UserEntity();
    private Project project = new Project();
    private TaskRequestDto taskRequestDto = new TaskRequestDto();
    private Task task = new Task();

    @BeforeEach
    public void init() {
        user.setUsername("testuser");
        user.setEmail("testemail");
        user.setPassword("testpassword");

        project.setId(1L);
        project.setName("testproject");
        project.setDescription("testdescription");
        project.setUser(user);

        taskRequestDto.setName("testtaskdto");
        taskRequestDto.setDescription("testdescriptiondto");
        taskRequestDto.setCompleted(false);

        task.setId(1L);
        task.setName("testtask");
        task.setDescription("testdescription");
        task.setCompleted(true);
        task.setProject(project);

        // Add code to mock the authentication object
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getUsername());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void TaskService_CreateTask_ReturnsTaskResponseDto() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));

        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto taskResponseDto = taskService.createTask(taskRequestDto, project.getId());
        Assertions.assertThat(taskResponseDto.getName()).isEqualTo(task.getName());
    }

    @Test
    public void TaskService_GetTasksByProjectId_ReturnsTaskResponseDtoList() {
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));

        when(taskRepository.findByProject(Mockito.any(Project.class))).thenReturn(List.of(task));

        List<TaskResponseDto> list = taskService.getTasksByProjectId(project.getId());
        Assertions.assertThat(list.get(0).getName()).isEqualTo(task.getName());
    }

    @Test
    public void TaskService_UpdateTask_ReturnsTaskResponseDto() {
        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task));
        when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));

        when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        TaskResponseDto response = taskService.updateTask(task.getId(), taskRequestDto, project.getId());
        Assertions.assertThat(response.getName()).isEqualTo(taskRequestDto.getName());
    }

    @Test
    public void TaskService_DeleteTask_ReturnsVoid() {
        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task));

        assertAll(() -> taskService.deleteTask(task.getId(), project.getId()));
    }
}
