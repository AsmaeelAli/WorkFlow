package com.work.flow;


import com.work.flow.cli.Cli;
import com.work.flow.repository.reminder.JpaReminderRepo;
import com.work.flow.repository.tasks.JpaTaskRepo;
import com.work.flow.repository.users.JpaUserRepo;
import com.work.flow.usecase.reminder.ReminderObserver;
import com.work.flow.usecase.reminder.ReminderObserverImpl;
import com.work.flow.usecase.reminder.ReminderScheduler;
import com.work.flow.usecase.reminder.ReminderService;
import com.work.flow.usecase.tasks.TaskService;
import com.work.flow.usecase.users.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class Main {

    static void main() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("WorkFlow");
        EntityManager entityManager = emf.createEntityManager();

        JpaUserRepo userRepo = new JpaUserRepo(entityManager);
        JpaTaskRepo taskRepo = new JpaTaskRepo(entityManager);
        JpaReminderRepo reminderRepo = new JpaReminderRepo(entityManager);

        UserService userService = new UserService(userRepo);
        ReminderService reminderService = new ReminderService(reminderRepo);
        TaskService taskService = new TaskService(taskRepo, reminderService);


        ReminderScheduler scheduler = new ReminderScheduler(reminderService);
        ReminderObserver observer = new ReminderObserverImpl();

        scheduler.subscribe(observer);
        scheduler.start();

        Cli cli = new Cli(taskService , userService);
        cli.start();

        scheduler.shutdown();


    }
}





