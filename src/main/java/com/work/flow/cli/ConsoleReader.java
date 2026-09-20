package com.work.flow.cli;

import com.work.flow.common.enums.NotificationChannel;
import com.work.flow.common.enums.Priority;

import java.util.Scanner;
import java.util.UUID;

public class ConsoleReader {
    private static ConsoleReader instance;
    private final Scanner scanner;

    private ConsoleReader() {
        this.scanner = new Scanner(System.in);
    }

    public static ConsoleReader getInstance() {
        if (instance == null) {
            instance = new ConsoleReader();
        }
        return instance;
    }

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            String input = readString(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(">> Please enter a valid number.");
            }
        }
    }

    public UUID readUUID(String prompt) {
        while (true) {
            String input = readString(prompt);
            try {
                return UUID.fromString(input);
            } catch (IllegalArgumentException e) {
                System.out.println(">> Invalid UUID format, please try again.");
            }
        }
    }

    public Priority readPriority() {
        System.out.println("Priority: 1=LOW, 2=MEDIUM, 3=HIGH");
        while (true) {
            switch (readInt("Choose: ")) {
                case 1 -> { return Priority.LOW; }
                case 2 -> { return Priority.MEDIUM; }
                case 3 -> { return Priority.HIGH; }
                default -> System.out.println(">> Please choose 1, 2 or 3.");
            }
        }
    }

    public NotificationChannel readChannel() {
        System.out.println("Notification Channel: 1=EMAIL, 2=SMS, 3=PUSH");

        while (true) {
            switch (readInt("Choose: ")) {
                case 1 -> { return NotificationChannel.EMAIL; }
                case 2 -> { return NotificationChannel.SMS; }
                case 3 -> { return NotificationChannel.PUSH; }
                default -> System.out.println(">> Please choose 1, 2 or 3.");
            }
        }
    }
}