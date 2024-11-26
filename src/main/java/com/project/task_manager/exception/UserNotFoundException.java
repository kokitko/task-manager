package com.project.task_manager.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
  @Serial
    private static final long serialVersionUID = 4L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
