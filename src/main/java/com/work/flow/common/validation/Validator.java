package com.work.flow.common.validation;

import com.work.flow.exception.InvalidFormatException;

import java.util.regex.Pattern;

public final class Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern USERNAME_PATTREN = Pattern.compile(
            "^[0-9A-Za-z]{3,16}$"
    );

    private static final Pattern TASK_NAME_PATTERN = Pattern.compile(
            "^[0-9A-Za-z]{3,20}$"
    );


    private Validator() {
    }

    public static void emailValid(String email) {
        if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidFormatException("Invalid username or email Format");
        }
    }

    public static void usernameValid(String username) {
        if (username == null || username.isBlank() || !USERNAME_PATTREN.matcher(username.trim()).matches()) {
            throw new InvalidFormatException("Invalid username or email Format");
        }
    }

    public static void taskNameValid(String name) {
        if (name == null || name.isBlank() || !TASK_NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new InvalidFormatException("Invalid task name (3-50 letters, digits or spaces)");
        }
    }

}
