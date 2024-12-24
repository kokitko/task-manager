package com.project.task_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponsePage {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private List<ProjectResponseDto> projects;
}
