package com.project.task_manager.service.impl;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.BelongingException;
import com.project.task_manager.exception.ProjectNotFoundException;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;

    @Autowired
    public AdminServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
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
    public List<ProjectResponseDto> getProjectsByUser(UserEntity user) {
        List<Project> projects = projectRepository.findByUser(user);
        return projects.stream()
                .map(this::mapToProjectDto)
                .toList();
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
