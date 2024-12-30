package com.project.task_manager.controller;

import com.project.task_manager.dto.*;
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

    @GetMapping("/users")
    public ResponseEntity<UserResponsePage> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getUsers(page, size));
    }

    @GetMapping("/user/{userId}/projects/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProject(@PathVariable("userId") Long userId,
                                                         @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(adminService.getProject(projectId, userId));
    }

    @PostMapping("/user/{userId}/projects")
    public ResponseEntity<ProjectResponseDto> createProject(@PathVariable("userId") Long userId,
                                                            @RequestBody ProjectRequestDto project) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ResponseEntity.ok(adminService.createProject(project, user));
    }

    @GetMapping("/user/{userId}/projects")
    public ResponseEntity<ProjectResponsePage> getProjectsByUser(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return ResponseEntity.ok(adminService.getProjectsByUser(user, page, size));
    }

    @PutMapping("/user/{userId}/projects/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable("userId") Long userId,
                                                            @PathVariable("projectId") Long projectId,
                                                            @RequestBody ProjectRequestDto project) {
        return ResponseEntity.ok(adminService.updateProject(projectId, project, userId));
    }

    @DeleteMapping("/user/{userId}/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable("userId") Long userId,
                                              @PathVariable("projectId") Long projectId) {
        adminService.deleteProject(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/{userId}/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable("userId") Long userId,
                                                      @PathVariable("projectId") Long projectId,
                                                      @RequestBody TaskRequestDto task) {
        return ResponseEntity.ok(adminService.createTask(task, projectId, userId));
    }

    @GetMapping("/user/{userId}/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProject(@PathVariable("userId") Long userId,
                                                                   @PathVariable("projectId") Long projectId,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "size", defaultValue = "5") int size) {
        return ResponseEntity.ok(adminService.getTasksByProject(projectId, userId));
    }

    @PutMapping("/user/{userId}/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable("userId") Long userId,
                                                      @PathVariable("projectId") Long projectId,
                                                      @PathVariable("taskId") Long taskId,
                                                      @RequestBody TaskRequestDto task) {
        return ResponseEntity.ok(adminService.updateTask(taskId, task, projectId, userId));
    }

    @DeleteMapping("/user/{userId}/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("userId") Long userId,
                                           @PathVariable("projectId") Long projectId,
                                           @PathVariable("taskId") Long taskId) {
        adminService.deleteTask(taskId, projectId, userId);
        return ResponseEntity.noContent().build();
    }
}
