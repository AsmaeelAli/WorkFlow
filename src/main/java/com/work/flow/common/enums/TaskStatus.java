package com.work.flow.common.enums;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE,
    OVERDUE,
    CANCELED;

    public boolean isTerminal() {
        return this == DONE || this == CANCELED;
    }

    public boolean isActive() {
        return this == TODO || this == IN_PROGRESS;
    }
}
