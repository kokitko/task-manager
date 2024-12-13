package com.project.task_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskResponseDto {
    private Long id;
    private String name;
    private String description;
    private boolean completed;
    private Long projectId;
}
