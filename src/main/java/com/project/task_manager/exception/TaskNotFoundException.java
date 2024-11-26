package com.project.task_manager.exception;

import java.io.Serial;

public class TaskNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2L;

    public TaskNotFoundException(String message) {
        super(message);
    }
}
