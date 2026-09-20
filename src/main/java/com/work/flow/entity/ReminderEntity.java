package com.work.flow.entity;

import com.work.flow.common.enums.NotificationChannel;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/*
عرفت اندكس داخل الجدول بحيث يفهرس التواريخ بشكل افضل ويكون الاستعلام اسرع
الفهرس مكون من التاريخ وحالة الارسال
السبب ببساطة لانه ممكن الاستعلام يرجع بعث التذكير اكثر من مرة وهشي غلط
فا كل عملية ارسال لليوزر بتتحول القيمة true
 */
@Entity
@Table(
        name = "reminders",
        indexes = {
                @Index(name = "idx_reminder_fetch", columnList = "reminder_Time, sent")
        }
)
public class ReminderEntity {

    @Id
    @Column
    private UUID id;

    @JoinColumn(name = "task_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TaskEntity task;

    @Column(name = "reminder_Time", nullable = false)
    private LocalDateTime reminderTime;

    @Column(name = "sent", nullable = false)
    private boolean sent;

    @Column(name = "enable" , nullable = false)
    private boolean enable;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    protected ReminderEntity() {
    }

    public ReminderEntity(TaskEntity task, LocalDateTime reminderTime , NotificationChannel channel) {
        this.id = UUID.randomUUID();
        this.task = task;
        this.reminderTime = reminderTime;
        this.channel = channel;
        this.sent = false;
        this.enable = true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isSent() {
        return sent;
    }

    public void markAsSent() {
        this.sent = true;
    }

    public boolean isEnable() {return enable;}

    public void cancel() {
        this.sent = false;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }
}
