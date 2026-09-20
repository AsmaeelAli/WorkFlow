package com.work.flow.usecase.reminder;

import com.work.flow.common.enums.NotificationChannel;
import com.work.flow.entity.ReminderDto;
import com.work.flow.entity.ReminderEntity;
import com.work.flow.entity.TaskEntity;
import com.work.flow.exception.EntityNotFoundException;
import com.work.flow.notification.factory.ReminderFactory;
import com.work.flow.notification.factory.ReminderSender;
import com.work.flow.repository.reminder.JpaReminderRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReminderService {
    private final JpaReminderRepo reminderRepo;

    public ReminderService(JpaReminderRepo reminderRepo) {
        this.reminderRepo = reminderRepo;
    }

    public void createReminder(TaskEntity task , NotificationChannel channel) {
        try {
            LocalDateTime reminderdate = task.getCompletedAt().minusDays(1);
            ReminderEntity reminder = new ReminderEntity(task,reminderdate,channel);
            reminderRepo.save(reminder);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void cancelReminder(UUID reminderId) {
        try {
            ReminderEntity reminder = reminderRepo.findById(reminderId)
                    .orElseThrow(() -> new EntityNotFoundException("Reminder Not Found"));
            reminder.cancel();
            reminderRepo.save(reminder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<ReminderDto> getPendingReminders(LocalDateTime now) {
        try {
            List<ReminderEntity> pendingReminders = reminderRepo.findPendingReminders(now);
            pendingReminders.forEach(ReminderEntity::markAsSent);
            reminderRepo.saveAll(pendingReminders);

            return pendingReminders.stream()
                    .map(reminder -> new ReminderDto(
                           reminder.getTask().getId().toString(),
                            reminder.getTask().getTaskName(),
                            reminder.getTask().getUser().getUsername(),
                            reminder.getTask().getUser().getEmail(),
                            reminder.getTask().getCompletedAt().toString(),
                            reminder.getChannel()
                    ))
                    .toList();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
