package com.work.flow.exception;

public class DuplicateUserException extends GlobalException {
    public DuplicateUserException(String email) {
        super("A user with email '" + email + "' already exists.");
    }
}
