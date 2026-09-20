package com.work.flow.exception;

public class GlobalException extends RuntimeException {
    public GlobalException(String message) {
        super("[Error --> " + message + " ]");
    }

    public GlobalException(String message, Throwable cause) {
        super("[Error --> " + message + "]", cause);
    }
}
