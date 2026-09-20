package com.work.flow.cli;


import com.work.flow.common.enums.NotificationChannel;
import com.work.flow.common.enums.Priority;
import com.work.flow.common.enums.TaskStatus;
import com.work.flow.entity.TaskDto;
import com.work.flow.entity.TaskEntity;
import com.work.flow.entity.UserEntity;
import com.work.flow.usecase.reminder.ReminderService;
import com.work.flow.usecase.tasks.TaskService;
import com.work.flow.usecase.users.UserService;


import java.util.List;
import java.util.UUID;

public class TaskCli {
    private final ConsoleReader reader = ConsoleReader.getInstance();
    private final TaskService taskService;

    public TaskCli(TaskService taskService) {
        this.taskService = taskService;
    }

    public void run(UserEntity user) {
        boolean inSession = true;
        while (inSession) {
            System.out.println("\n--- Main Menu (" + user.getUsername() + ") ---");
            System.out.println("0. Logout");
            System.out.println("\n Task Management Options:");
            System.out.println("1. Create Task");
            System.out.println("2. Change Task Status");
            System.out.println("3. Find My Tasks");
            System.out.println("\n--- Public Tasks ---");
            System.out.println("\n6. Find Unclaimed Tasks");
            System.out.println("7. Claim Task");
            System.out.println("\n--- Reports ---");
            System.out.println("\n8. Show Due Soon Tasks");
            System.out.println("9. Show Statistics");
            switch (reader.readInt("Choose: ")) {
                case 0 -> inSession = false;
                case 1 -> handleCreateTask(user);
                case 2 -> handleChangeStatus(user);
                case 3 -> handleFindMyTasks(user);
                default -> System.out.println(">> Invalid choice, please try again.");
            }
        }
    }

    private void handleFindMyTasks(UserEntity user) {
        try{
            System.out.println("Your tasks:");
            List<TaskDto> tasks = taskService.getMyTasks(user);
            if (tasks.isEmpty()) {
                System.out.println(">> No tasks found.");
            } else {
                for (TaskDto task : tasks) {
                    System.out.println("ID: " + task.taskId() + " , Name: " + task.name() + " , Status: " + task.status());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleChangeStatus(UserEntity user) {
        UUID taskId = reader.readUUID("Enter Task ID to change status: ");
        try {
            System.out.println("Select new status: 0=Cancel , 1=TODO , 2=IN_PROGRESS , 3=OVERDUE , 4=DONE ");
            int statusChoice = reader.readInt("Choose: ");
            TaskStatus newStatus;
            switch (statusChoice) {
                case 0 -> newStatus = TaskStatus.CANCELED;
                case 1 -> newStatus = TaskStatus.TODO;
                case 2 -> newStatus = TaskStatus.IN_PROGRESS;
                case 3 -> newStatus = TaskStatus.OVERDUE;
                case 4 -> newStatus = TaskStatus.DONE;
                default -> {
                    System.out.println(">> Invalid status choice.");
                    return;
                }
            }
            taskService.changeStatus(taskId, newStatus);
            System.out.println(">> Task status updated successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleCreateTask(UserEntity user) {
        String name = reader.readString("Task name: ");
        Priority priority = reader.readPriority();
        System.out.println("Task Status will be set to (TODO) by default.");
        int dueDate = reader.readInt("Due date in days: ");
        NotificationChannel channel = reader.readChannel();
        String flag = reader.readString("Assign task to yourself? (Y/N): ");
        try {
            TaskEntity task;
            switch (flag)
            {
                case "Y", "y" -> {
                    task = taskService.createTask(name, priority,user, dueDate , channel);
                    System.out.println(">> Task created and assigned to you: " + task.getTaskName());
                }
                case "N", "n" -> {
                    task = taskService.createTask(name, priority,null, dueDate , channel);
                    System.out.println(">> Task created and unassigned: " + task.getTaskName());
                }
                default -> System.out.println(">> Invalid input. Task created but not assigned.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
