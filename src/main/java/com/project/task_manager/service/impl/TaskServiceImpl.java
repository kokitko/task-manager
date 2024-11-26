package com.project.task_manager.service.impl;

import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import com.project.task_manager.exception.ProjectNotFoundException;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskDto) {
        Project project = projectRepository.findById(taskDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!checkIfRequesterIsOwner(project)) {
            throw new RuntimeException("You are not the owner of this project");
        }
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setCompleted(taskDto.isCompleted());
        task.setProject(project);

        Task savedTask = taskRepository.save(task);
        return mapToResponseDto(savedTask);
    }

    @Override
    public List<TaskResponseDto> getTasksByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!checkIfRequesterIsOwner(project)) {
            throw new RuntimeException("You are not the owner of this project");
        }

        return taskRepository.findByProject(project).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto taskDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Project project = projectRepository.findById(taskDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!checkIfRequesterIsOwner(project)) {
            throw new RuntimeException("You are not the owner of this project");
        }

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setCompleted(taskDto.isCompleted());
        Task updatedTask = taskRepository.save(task);

        return mapToResponseDto(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!checkIfRequesterIsOwner(task.getProject())) {
            throw new RuntimeException("You are not the owner of this project");
        }

        taskRepository.deleteById(taskId);
    }

    private TaskResponseDto mapToResponseDto(Task task) {
        TaskResponseDto taskDto = new TaskResponseDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setCompleted(task.isCompleted());
        taskDto.setProjectId(task.getProject().getId());
        return taskDto;
    }

    private boolean checkIfRequesterIsOwner(Project project) {
        return project.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
