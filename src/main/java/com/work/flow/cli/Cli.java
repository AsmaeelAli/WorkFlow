package com.work.flow.cli;

import com.work.flow.usecase.reminder.ReminderService;
import com.work.flow.usecase.tasks.TaskService;
import com.work.flow.usecase.users.UserService;

import java.util.NoSuchElementException;

public class Cli {

    private final ConsoleReader reader = ConsoleReader.getInstance();
    private final UserCli userCli;
    private final TaskCli taskCli;

    public Cli(TaskService taskService, UserService userService) {
        this.userCli = new UserCli(userService);
        this.taskCli = new TaskCli(taskService);
    }

    public void start() {
        printBanner();
        boolean running = true;
        while (running) {
            try {
                printAuthMenu();
                switch (reader.readInt("Choose: ")) {
                    case 1 -> userCli.handleLogin().ifPresent(taskCli::run);
                    case 2 -> userCli.handleRegister().ifPresent(taskCli::run);
                    case 0 -> running = false;
                    default -> System.out.println(">> Invalid choice, please try again.");
                }
            } catch (NoSuchElementException e) {
                running = false;
            } catch (RuntimeException e) {
                System.out.println(">> Unexpected error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private void printBanner() {
        System.out.println("==========================================");
        System.out.println("   WELCOME TO WORKFLOW MANAGEMENT SYSTEM  ");
        System.out.println("==========================================");
    }


    private void printAuthMenu() {
        System.out.println("\n--- Authentication Menu ---");
        System.out.println("1. Login with Email");
        System.out.println("2. Register new user");
        System.out.println("0. Exit Application");
    }
}
