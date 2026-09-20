package com.work.flow.notification.factory;

import com.work.flow.entity.ReminderDto;
public class SmsReminderSender implements ReminderSender {

    @Override
    public void send(ReminderDto reminder) {
        System.out.println(
                "[SMS] Hi " + reminder.username()
                        + ", reminder: " + reminder.taskName()
                        + " is due at " + reminder.dueDate() + "."
        );
    }
}
