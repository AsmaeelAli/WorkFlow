package com.work.flow.usecase.tasks;

import com.work.flow.common.enums.NotificationChannel;
import com.work.flow.common.enums.Priority;
import com.work.flow.common.enums.TaskStatus;
import com.work.flow.common.validation.Validator;
import com.work.flow.entity.TaskDto;
import com.work.flow.entity.TaskEntity;
import com.work.flow.entity.UserEntity;
import com.work.flow.exception.DatabaseException;
import com.work.flow.exception.EntityNotFoundException;
import com.work.flow.exception.GlobalException;
import com.work.flow.repository.tasks.JpaTaskRepo;
import com.work.flow.usecase.reminder.ReminderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public class TaskService {

    private final JpaTaskRepo taskRepo;
    private final ReminderService reminderService;

    public TaskService(JpaTaskRepo taskRepo , ReminderService reminderService) {
        this.taskRepo = taskRepo;
        this.reminderService = reminderService;
    }

    public TaskEntity createTask(String taskName, Priority priority
            , UserEntity user, int dueDate , NotificationChannel channel) {

        Validator.taskNameValid(taskName);
        TaskEntity task = new TaskEntity(
                taskName,
                user,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(dueDate),
                TaskStatus.TODO,
                priority
        );

        if(user != null) {
            task.markAsClaimed();
        }

        taskRepo.save(task);
        reminderService.createReminder(task,channel);
        return task;
    }

    public void changeStatus(UUID id,TaskStatus status) {
            TaskEntity task = taskRepo.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + id));
            task.setTaskStatus(status);
            taskRepo.save(task);
    }

    public List<TaskDto> getMyTasks(UserEntity user) {
        List<TaskEntity> tasks = taskRepo.findAllByUserId(user.getId());
        return tasks.stream().map(
                task -> new TaskDto(task.getId() , task.getTaskName(), task.getTaskStatus().toString()))
                .toList();
    }

    public synchronized void claimTask(UUID taskId, UserEntity user) {
        try {
            TaskEntity task = taskRepo.findById(taskId)
                    .orElseThrow(() -> new DatabaseException("Task not found with ID: " + taskId));
            if (task.getUser() != null) {
                throw new GlobalException("Task is already claimed by another user.");
            }
            task.setUser(user);
            changeStatus(taskId,TaskStatus.IN_PROGRESS);
            taskRepo.save(task);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
