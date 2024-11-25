package com.project.task_manager.service;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.entity.UserEntity;

import java.util.List;

public interface AdminService {

    ProjectResponseDto createProject(ProjectRequestDto project, UserEntity user);
    List<ProjectResponseDto> getProjectsByUser(UserEntity user);
    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto project);
    void deleteProject(Long projectId);
}
