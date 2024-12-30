package com.project.task_manager.service.impl;

import com.project.task_manager.dto.*;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.BelongingException;
import com.project.task_manager.exception.ProjectNotFoundException;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;

    @Autowired
    public AdminServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserResponsePage getUsers(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<UserEntity> users = userRepository.findAll(pageable);
        List<UserInfoResponse> userEntities = users.stream().map(this::mapToUserDto).toList();

        UserResponsePage userResponsePage = new UserResponsePage();
        userResponsePage.setPage(users.getNumber());
        userResponsePage.setSize(users.getSize());
        userResponsePage.setTotalPages(users.getTotalPages());
        userResponsePage.setTotalElements(users.getTotalElements());
        userResponsePage.setLast(users.isLast());
        userResponsePage.setUsers(userEntities);
        return userResponsePage;
    }

    @Override
    public ProjectResponseDto getProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!project.getUser().getId().equals(userId)) {
            throw new BelongingException("This project does not belong to the submitted user");
        }
        return mapToProjectDto(project);
    }

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto project, UserEntity user) {
        Project projectEntity = Project.builder()
                .name(project.getName())
                .description(project.getDescription())
                .user(user)
                .build();
        Project createdProject = projectRepository.save(projectEntity);
        return mapToProjectDto(createdProject);
    }

    @Override
    public ProjectResponsePage getProjectsByUser(UserEntity user, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Project> projects = projectRepository.findByUser(user, pageable);
        List<ProjectResponseDto> projectResponseDtos = projects.stream().map(this::mapToProjectDto).toList();

        ProjectResponsePage projectResponsePage = new ProjectResponsePage();
        projectResponsePage.setPage(projects.getNumber());
        projectResponsePage.setSize(projects.getSize());
        projectResponsePage.setTotalPages(projects.getTotalPages());
        projectResponsePage.setTotalElements(projects.getTotalElements());
        projectResponsePage.setLast(projects.isLast());
        projectResponsePage.setProjects(projectResponseDtos);
        return projectResponsePage;
    }

    @Override
    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto projectDto, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!project.getUser().getId().equals(userId)) {
            throw new BelongingException("This project does not belong to the submitted user");
        }
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());

        Project savedProject = projectRepository.save(project);
        return mapToProjectDto(savedProject);
    }

    @Override
    public void deleteProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId).orElseThrow(()
                -> new ProjectNotFoundException("Project not found"));
        if (!project.getUser().getId().equals(userId)) {
            throw new BelongingException("This project does not belong to the submitted user");
        }
        projectRepository.delete(project);
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto task, Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!project.getUser().getId().equals(userId)) {
            throw new BelongingException("This project does not belong to the submitted user");
        }
        Task taskToSave = Task.builder()
                .name(task.getName())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .project(project)
                .build();
        Task savedTask = taskRepository.save(taskToSave);
        return mapToTaskDto(savedTask);
    }

    @Override
    public List<TaskResponseDto> getTasksByProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!project.getUser().getId().equals(userId)) {
            throw new BelongingException("This project does not belong to the submitted user");
        }
        List<Task> tasks = taskRepository.findByProject(project);
        return tasks.stream().map(this::mapToTaskDto).toList();
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto task, Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!project.getUser().getId().equals(userId)) {
            throw new BelongingException("This project does not belong to the submitted user");
        }
        Task taskToUpdate = taskRepository.findById(taskId)
                .orElseThrow(() -> new ProjectNotFoundException("Task not found"));
        taskToUpdate.setName(task.getName());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setCompleted(task.isCompleted());
        Task savedTask = taskRepository.save(taskToUpdate);
        return mapToTaskDto(savedTask);
    }

    @Override
    public void deleteTask(Long taskId, Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!project.getUser().getId().equals(userId)) {
            throw new BelongingException("This project does not belong to the submitted user");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ProjectNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    private UserInfoResponse mapToUserDto(UserEntity user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    private TaskResponseDto mapToTaskDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .projectId(task.getProject().getId())
                .build();

    }

    private ProjectResponseDto mapToProjectDto(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .userId(project.getUser().getId())
                .build();
    }
}
