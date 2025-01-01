package com.project.task_manager.repository;

import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class TaskRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    UserEntity user = new UserEntity();
    Project project1 = new Project();
    Task task1 = new Task();
    Task task2 = new Task();

    @BeforeEach
    public void init() {
        user.setUsername("testusername");
        user.setEmail("testemail");
        user.setPassword("testpassword");
        userRepository.save(user);

        project1.setName("testproject1");
        project1.setDescription("testdescription1");
        project1.setUser(user);
        projectRepository.save(project1);

        task1.setName("testtask1");
        task1.setDescription("testdescription1");
        task1.setProject(project1);

        task2.setName("testtask2");
        task2.setDescription("testdescription2");
        task2.setProject(project1);
    }

    @Test
    public void TaskRepository_SaveAllTasks_ReturnsAllTasks() {
        List<Task> list = taskRepository.saveAll(List.of(task1, task2));

        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void TaskRepository_FindByProject_ReturnsPage() {
        taskRepository.saveAll(List.of(task1, task2));
        Pageable pageable = Pageable.ofSize(2).withPage(0);

        Page<Task> page = taskRepository.findByProject(project1, pageable);

        Assertions.assertThat(page).isNotNull();
        Assertions.assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void TaskRepository_SaveTask_ReturnsTask() {
        Task task = taskRepository.save(task1);

        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.getId()).isNotNull();
    }

    @Test
    public void TaskRepository_FindByProject_ReturnsProjectTasks() {
        taskRepository.saveAll(List.of(task1, task2));

        List<Task> list = taskRepository.findByProject(project1);

        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void TaskRepository_FindById_ReturnsTask() {
        Task savedTask = taskRepository.save(task1);

        Task task = taskRepository.findById(savedTask.getId()).orElseThrow(
                () -> new IllegalArgumentException("Task not found")
        );

        Assertions.assertThat(task).isNotNull();
    }

    @Test
    public void TaskRepository_UpdateTask_ReturnsTask() {
        Task savedTask = taskRepository.save(task1);

        savedTask.setName("updatedname");
        savedTask.setDescription("updateddescription");

        Task updatedTask = taskRepository.save(savedTask);

        Assertions.assertThat(updatedTask).isNotNull();
        Assertions.assertThat(updatedTask.getId()).isEqualTo(savedTask.getId());
        Assertions.assertThat(updatedTask.getName()).isEqualTo("updatedname");
        Assertions.assertThat(updatedTask.getDescription()).isEqualTo("updateddescription");
    }

    @Test
    public void TaskRepository_DeleteTask_ReturnsNull() {
        Task savedTask = taskRepository.save(task1);

        taskRepository.deleteById(savedTask.getId());

        Optional<Task> task = taskRepository.findById(savedTask.getId());
        Assertions.assertThat(task).isEmpty();
    }
}
