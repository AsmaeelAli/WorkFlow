package com.work.flow.usecase.reminder;

import com.work.flow.entity.ReminderDto;

public interface ReminderObserver {
    void update(ReminderDto reminder);
}
