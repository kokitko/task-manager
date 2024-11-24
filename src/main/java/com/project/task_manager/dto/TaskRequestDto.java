package com.project.task_manager.dto;

import lombok.Data;

@Data
public class TaskRequestDto {
    private String name;
    private String description;
    private boolean completed;
    private Long projectId;
}
