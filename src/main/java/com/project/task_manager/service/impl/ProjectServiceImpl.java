package com.project.task_manager.service.impl;

import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project createProject(Project project, UserEntity user) {
        project.setUser(user);
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getProjectsByUser(UserEntity user) {
        return projectRepository.findByUser(user);
    }

    @Override
    public Project updateProject(Long projectId, Project project) {
        project.setId(projectId);
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}
