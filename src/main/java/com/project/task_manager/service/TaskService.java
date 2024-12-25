package com.project.task_manager.service;

import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;
import com.project.task_manager.dto.TaskResponsePage;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskDto, Long projectId);
    TaskResponsePage getTasksByProjectId(Long projectId, int page, int size);
    TaskResponseDto getTaskById(Long taskId, Long projectId);
    TaskResponseDto updateTask(Long taskId, TaskRequestDto taskDto, Long projectId);
    void deleteTask(Long taskId, Long projectId);
}
