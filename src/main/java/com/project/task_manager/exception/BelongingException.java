package com.project.task_manager.exception;

import java.io.Serial;

public class BelongingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5L;

    public BelongingException(String message) {
        super(message);
    }
}
