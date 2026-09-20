package com.work.flow.exception;

public class EntityNotFoundException extends GlobalException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
