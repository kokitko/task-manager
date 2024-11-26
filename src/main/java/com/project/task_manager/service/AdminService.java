package com.project.task_manager.service;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;
import com.project.task_manager.entity.UserEntity;

import java.util.List;

public interface AdminService {

    ProjectResponseDto createProject(ProjectRequestDto project, UserEntity user);
    List<ProjectResponseDto> getProjectsByUser(UserEntity user);
    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto project, Long userId);
    void deleteProject(Long projectId, Long userId);

    TaskResponseDto createTask(TaskRequestDto task, Long projectId, Long userId);
    List<TaskResponseDto> getTasksByProject(Long projectId, Long userId);
    TaskResponseDto updateTask(Long taskId, TaskRequestDto task, Long projectId, Long userId);
    void deleteTask(Long taskId, Long projectId, Long userId);
}
