package com.project.task_manager.service;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Role;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.service.impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
