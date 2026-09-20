package com.work.flow.notification.factory;

import com.work.flow.entity.ReminderDto;

public class PushReminderSender implements ReminderSender {

    @Override
    public void send(ReminderDto reminder) {
        System.out.printf(
                """
                        
                        +------------------------------------------------+
                        |                 IMPORTANT REMINDER             |
                        +------------------------------------------------+
                        | Task: %s
                        | Due:  %s
                        |
                        | Don't forget to complete this task!
                        +------------------------------------------------+
                        %n"""
                ,reminder.taskName()
                ,reminder.dueDate()
    );
    }
}
