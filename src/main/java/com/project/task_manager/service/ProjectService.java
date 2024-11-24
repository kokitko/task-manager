package com.project.task_manager.service;

import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;

import java.util.List;

public interface ProjectService {
    Project createProject(Project project, UserEntity user);
    List<Project> getProjectsByUser(UserEntity user);
    Project updateProject(Long projectId, Project project);
    void deleteProject(Long projectId);
}
