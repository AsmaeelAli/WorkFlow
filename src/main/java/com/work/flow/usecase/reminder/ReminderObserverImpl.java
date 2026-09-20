package com.work.flow.usecase.reminder;

import com.work.flow.entity.ReminderDto;
import com.work.flow.notification.factory.ReminderFactory;

public class ReminderObserverImpl implements  ReminderObserver {


    public ReminderObserverImpl() {
    }

    @Override
    public void update(ReminderDto reminder) {
        ReminderFactory.createSender(reminder.channel()).send(reminder);
    }
}
