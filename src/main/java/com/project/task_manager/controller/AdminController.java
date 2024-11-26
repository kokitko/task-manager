package com.project.task_manager.controller;

import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.exception.UserNotFoundException;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;
    private UserRepository userRepository;

    @Autowired
    public AdminController(AdminService adminService, UserRepository userRepository) {
        this.adminService = adminService;
        this.userRepository = userRepository;
    }

    @PostMapping("/user/{userId}/projects")
    public ResponseEntity<ProjectResponseDto> createProject(@PathVariable("userId") Long userId,
                                                            @RequestBody ProjectRequestDto project) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ResponseEntity.ok(adminService.createProject(project, user));
    }

    @GetMapping("/user/{userId}/projects")
    public ResponseEntity<List<ProjectResponseDto>> getProjectsByUser(@PathVariable("userId") Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ResponseEntity.ok(adminService.getProjectsByUser(user));
    }

    @PutMapping("/user/{userId}/projects/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable("userId") Long userId,
                                                            @PathVariable("projectId") Long projectId,
                                                            @RequestBody ProjectRequestDto project) {
        return ResponseEntity.ok(adminService.updateProject(projectId, project));
    }

    @DeleteMapping("/user/{userId}/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable("userId") Long userId,
                                              @PathVariable("projectId") Long projectId) {
        adminService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}
