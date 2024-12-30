package com.project.task_manager.service;

import com.project.task_manager.dto.*;
import com.project.task_manager.entity.UserEntity;

public interface AdminService {
    UserResponsePage getUsers(int page, int size);

    ProjectResponseDto getProject(Long projectId, Long userId);
    ProjectResponseDto createProject(ProjectRequestDto project, UserEntity user);
    ProjectResponsePage getProjectsByUser(UserEntity user, int  page, int size);
    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto project, Long userId);
    void deleteProject(Long projectId, Long userId);

    TaskResponseDto createTask(TaskRequestDto task, Long projectId, Long userId);
    TaskResponsePage getTasksByProject(Long projectId, Long userId, int page, int size);
    TaskResponseDto updateTask(Long taskId, TaskRequestDto task, Long projectId, Long userId);
    void deleteTask(Long taskId, Long projectId, Long userId);
}
