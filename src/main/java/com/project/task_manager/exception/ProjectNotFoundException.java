package com.project.task_manager.exception;

import java.io.Serial;

public class ProjectNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
