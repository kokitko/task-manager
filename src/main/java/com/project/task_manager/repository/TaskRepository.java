package com.project.task_manager.repository;

import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProject(Project project, Pageable pageable);
    List<Task> findByProject(Project project);
}
