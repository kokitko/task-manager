package com.project.task_manager.controller;

import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;
import com.project.task_manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{projectId}")
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable("projectId") Long projectId,
                                                      @RequestBody TaskRequestDto taskDto) {
        return ResponseEntity.ok(taskService.createTask(taskDto, projectId));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<TaskResponseDto>> getTasksByProject(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProjectId(projectId));
    }

    @PutMapping("/{projectId}/task/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable("taskId") Long taskId,
                                                      @PathVariable("projectId") Long projectId,
                                                      @RequestBody TaskRequestDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDto, projectId));
    }

    @DeleteMapping("/{projectId}/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("projectId") Long projectId,
                                           @PathVariable("projectId") Long taskId) {
        taskService.deleteTask(taskId, projectId);
        return ResponseEntity.noContent().build();
    }
}
