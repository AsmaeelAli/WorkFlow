package com.work.flow.entity;


import com.work.flow.common.enums.NotificationChannel;

public record ReminderDto(
        String taskId,
        String taskName,
        String username,
        String email,
        String dueDate,
        NotificationChannel channel
){}
