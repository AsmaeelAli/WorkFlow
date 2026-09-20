package com.work.flow.repository.reminder;

import com.work.flow.entity.ReminderEntity;
import com.work.flow.repository.jparepo.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReminderRepository extends Repository<ReminderEntity, UUID> {
    List<ReminderEntity> findPendingReminders(LocalDateTime now);
}
