package com.project.task_manager.service;

import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskDto);
    List<TaskResponseDto> getTasksByProjectId(Long projectId);
    TaskResponseDto updateTask(Long taskId, TaskRequestDto taskDto);
    void deleteTask(Long taskId);
}
