package com.project.task_manager.repository;

import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(UserEntity user);
}
