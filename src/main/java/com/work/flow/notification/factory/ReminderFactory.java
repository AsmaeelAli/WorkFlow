package com.work.flow.notification.factory;

import com.work.flow.common.enums.NotificationChannel;

public class ReminderFactory {

    private ReminderFactory() {
    }

    public static ReminderSender createSender(NotificationChannel channel) {
        return switch (channel){
            case EMAIL -> new EmailReminderSender();
            case SMS -> new SmsReminderSender();
            case PUSH -> new PushReminderSender();
        };
    }
}
