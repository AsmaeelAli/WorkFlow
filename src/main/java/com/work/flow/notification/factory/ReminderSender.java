package com.work.flow.notification.factory;

import com.work.flow.entity.ReminderDto;

public interface ReminderSender {
    void send(ReminderDto reminder);
}
