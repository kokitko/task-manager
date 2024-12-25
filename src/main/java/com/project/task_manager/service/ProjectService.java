package com.project.task_manager.service;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.dto.ProjectResponsePage;
import com.project.task_manager.entity.UserEntity;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto project, UserEntity user);
    ProjectResponseDto getProjectById(Long projectId);
    ProjectResponsePage getProjectsByUser(UserEntity user, int page, int size);
    ProjectResponseDto updateProject(Long projectId, ProjectRequestDto project);
    void deleteProject(Long projectId);
}
