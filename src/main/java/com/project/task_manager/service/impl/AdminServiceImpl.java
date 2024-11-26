package com.project.task_manager.service.impl;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.ProjectNotFoundException;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private ProjectRepository projectRepository;

    @Autowired
    public AdminServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto project, UserEntity user) {
        Project projectEntity = Project.builder()
                .name(project.getName())
                .description(project.getDescription())
                .user(user)
                .build();
        Project createdProject = projectRepository.save(projectEntity);
        return mapToProjectDto(createdProject);
    }

    @Override
    public List<ProjectResponseDto> getProjectsByUser(UserEntity user) {
        List<Project> projects = projectRepository.findByUser(user);
        return projects.stream()
                .map(this::mapToProjectDto)
                .toList();
    }

    @Override
    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto projectDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());

        Project savedProject = projectRepository.save(project);
        return mapToProjectDto(savedProject);
    }

    @Override
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(()
                -> new ProjectNotFoundException("Project not found"));
        projectRepository.delete(project);
    }

    private ProjectResponseDto mapToProjectDto(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .userId(project.getUser().getId())
                .build();
    }
}
