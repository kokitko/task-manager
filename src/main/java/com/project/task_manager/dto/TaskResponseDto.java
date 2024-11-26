package com.project.task_manager.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskResponseDto {
    private Long id;
    private String name;
    private String description;
    private boolean completed;
    private Long projectId;
}
