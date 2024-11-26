package com.project.task_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.NoPermissionException;
import java.nio.file.AccessDeniedException;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ErrorObject> handleProjectNotFoundException() {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(404);
        errorObject.setMessage("Project not found");
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorObject> handleTaskNotFoundException() {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(404);
        errorObject.setMessage("Task not found");
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIsNotOwnerException.class)
    public ResponseEntity<ErrorObject> handleUserIsNotOwnerException() {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(403);
        errorObject.setMessage("You can not work with this project/task");
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException() {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(404);
        errorObject.setMessage("User not found");
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }
}
