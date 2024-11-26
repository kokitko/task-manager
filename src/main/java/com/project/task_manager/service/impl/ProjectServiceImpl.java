package com.project.task_manager.service.impl;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.ProjectNotFoundException;
import com.project.task_manager.exception.UserIsNotOwnerException;
import com.project.task_manager.repository.ProjectRepository;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final UserRepository userRepository;
    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto, UserEntity user) {
        Project project = mapToProject(projectRequestDto);
        project.setUser(user);
        Project savedProject = projectRepository.save(project);
        return mapToProjectDto(savedProject);
    }

    @Override
    public List<ProjectResponseDto> getProjectsByUser(UserEntity user) {
        List<Project> projects = projectRepository.findByUser(user);
        return projects.stream().map(this::mapToProjectDto).toList();
    }

    @Override
    public ProjectResponseDto updateProject(Long projectId, ProjectRequestDto project) {
        Project projectToUpdate = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (!checkIfRequesterIsOwner(projectToUpdate)) {
            throw new UserIsNotOwnerException("You are not the owner of this project");
        }
        projectToUpdate.setName(project.getName());
        projectToUpdate.setDescription(project.getDescription());
        Project savedProject = projectRepository.save(projectToUpdate);
        return mapToProjectDto(savedProject);
    }

    @Override
    public void deleteProject(Long projectId) {
        Project projectToDelete = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        if (checkIfRequesterIsOwner(projectToDelete)) {
            projectRepository.delete(projectToDelete);
        } else throw new UserIsNotOwnerException("You are not the owner of this project");
    }

    private ProjectResponseDto mapToProjectDto(Project savedProject) {
        return ProjectResponseDto.builder()
                .id(savedProject.getId())
                .name(savedProject.getName())
                .description(savedProject.getDescription())
                .userId(savedProject.getUser().getId())
                .build();
    }

    private Project mapToProject(ProjectRequestDto projectRequestDto) {
        return Project.builder()
                .name(projectRequestDto.getName())
                .description(projectRequestDto.getDescription())
                .build();
    }

    private boolean checkIfRequesterIsOwner(Project project) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (project.getUser().getUsername().equals(username)) {
            return true;
        } else {
            return false;
        }
    }
}
