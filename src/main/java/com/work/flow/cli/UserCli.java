package com.work.flow.cli;

import com.work.flow.entity.UserEntity;
import com.work.flow.usecase.users.UserService;

import java.util.Optional;

public class UserCli {

    private final ConsoleReader reader = ConsoleReader.getInstance();
    private final UserService userService;

    public UserCli(UserService userService) {
        this.userService = userService;
    }

    public Optional<UserEntity> handleLogin() {
        String email = reader.readString("Email: ");
        try {
            return Optional.of(userService.login(email));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<UserEntity> handleRegister() {
        String name = reader.readString("Name: ");
        String email = reader.readString("Email: ");
        try {
            return Optional.of(userService.createUser(name, email));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
