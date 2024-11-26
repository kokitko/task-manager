package com.project.task_manager.exception;

import java.io.Serial;

public class UserIsNotOwnerException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3L;

    public UserIsNotOwnerException(String message) {
        super(message);
    }
}
