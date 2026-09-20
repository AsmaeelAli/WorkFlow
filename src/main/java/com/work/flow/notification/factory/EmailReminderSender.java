package com.work.flow.notification.factory;

import com.work.flow.entity.ReminderDto;

public class EmailReminderSender implements ReminderSender {

    @Override
    public void send(ReminderDto reminder) {
        System.out.println("""
            
            ==================== EMAIL ====================
            To: %s
            Subject: Reminder - %s
            
            Hello %s,
            
            This is a reminder for your task:
            
            Task: %s
            Due Date: %s
            
            Please make sure to complete it on time.
            
            =================================================
            """.formatted(
                reminder.email(),
                reminder.taskName(),
                reminder.username(),
                reminder.taskName(),
                reminder.dueDate()
        ));
    }
}
