package com.work.flow.entity;

import com.work.flow.common.enums.Priority;
import com.work.flow.common.enums.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class TaskEntity {

    @Id
    @Column
    private UUID id;

    @Column
    private String taskName;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(nullable = false)
    private boolean claimed;

    protected TaskEntity() {
    }

    public TaskEntity(String taskName, UserEntity user, LocalDateTime createdAt
            ,LocalDateTime completedAt, TaskStatus taskStatus, Priority priority) {

        this.id = UUID.randomUUID();
        this.taskName = taskName;
        this.user = user;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.taskStatus = taskStatus;
        this.priority = priority;
        this.claimed = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void markAsClaimed() {
        this.claimed = true;
    }
}
