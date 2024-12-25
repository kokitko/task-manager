package com.project.task_manager.service.impl;

import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;
import com.project.task_manager.dto.TaskResponsePage;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import com.project.task_manager.exception.ProjectNotFoundException;
import com.project.task_manager.exception.TaskNotFoundException;
import com.project.task_manager.exception.UserIsNotOwnerException;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.TaskRepository;
import com.project.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public TaskResponseDto createTask(TaskRequestDto taskDto, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!checkIfRequesterIsOwner(project)) {
            throw new UserIsNotOwnerException("You are not the owner of this project");
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
    public TaskResponsePage getTasksByProjectId(Long projectId, int page, int size) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!checkIfRequesterIsOwner(project)) {
            throw new UserIsNotOwnerException("You are not the owner of this project");
        }
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Task> tasks = taskRepository.findByProject(project, pageable);
        List<TaskResponseDto> taskDtos = tasks.stream().map(this::mapToResponseDto).toList();

        TaskResponsePage taskResponsePage = new TaskResponsePage();
        taskResponsePage.setPage(tasks.getNumber());
        taskResponsePage.setSize(tasks.getSize());
        taskResponsePage.setTotalPages(tasks.getTotalPages());
        taskResponsePage.setTotalElements(tasks.getTotalElements());
        taskResponsePage.setLast(tasks.isLast());
        taskResponsePage.setTasks(taskDtos);
        return taskResponsePage;
    }

    @Override
    public TaskResponseDto getTaskById(Long taskId, Long projectId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!checkIfRequesterIsOwner(task.getProject())) {
            throw new UserIsNotOwnerException("You are not the owner of this project");
        }
        return mapToResponseDto(task);
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, TaskRequestDto taskDto, Long projectId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!checkIfRequesterIsOwner(project)) {
            throw new UserIsNotOwnerException("You are not the owner of this project");
        }

        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setCompleted(taskDto.isCompleted());
        Task updatedTask = taskRepository.save(task);

        return mapToResponseDto(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId, Long projectId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        if (!checkIfRequesterIsOwner(task.getProject())) {
            throw new UserIsNotOwnerException("You are not the owner of this project");
        }

        taskRepository.deleteById(taskId);
    }

    private TaskResponseDto mapToResponseDto(Task task) {
        TaskResponseDto taskDto = TaskResponseDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .projectId(task.getProject().getId())
                .build();
        return taskDto;
    }

    private boolean checkIfRequesterIsOwner(Project project) {
        return project.getUser().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
