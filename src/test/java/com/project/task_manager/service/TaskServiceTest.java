package com.project.task_manager.service;

import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

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
    private Task task1 = new Task();
    private Task task2 = new Task();

    @BeforeEach
    public void init() {
        user.setUsername("testuser");
        user.setEmail("testemail");
        user.setPassword("testpassword");

        project.setId(1L);
        project.setName("testproject");
        project.setDescription("testdescription");

        task1.setId(1L);
        task1.setName("testtask1");
        task1.setDescription("testdescription1");
        task1.setCompleted(false);
        task1.setProject(project);

        task2.setId(2L);
        task2.setName("testtask2");
        task2.setDescription("testdescription2");
        task2.setCompleted(true);
        task2.setProject(project);
    }
}
