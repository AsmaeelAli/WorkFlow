package com.work.flow.usecase.reminder;

import com.work.flow.entity.ReminderDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



public class ReminderScheduler {
    private final ReminderService reminderService;
    private final List<ReminderObserver> observers = new ArrayList<>();
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public ReminderScheduler(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    public void subscribe(ReminderObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(ReminderObserver observer) {
        observers.remove(observer);
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkReminders();
            } catch (Exception e) {
                System.out.println("Error sending reminders: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private void checkReminders() {

        LocalDateTime now = LocalDateTime.now();
        List<ReminderDto> reminders = reminderService.getPendingReminders(now);

        for (ReminderDto reminder : reminders) {
            notifyObservers(reminder);
        }
    }

    private void notifyObservers(ReminderDto reminder) {
        for (ReminderObserver observer : observers) {
            observer.update(reminder);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}


