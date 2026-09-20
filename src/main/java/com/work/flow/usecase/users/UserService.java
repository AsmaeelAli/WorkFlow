package com.work.flow.usecase.users;

import com.work.flow.common.validation.Validator;
import com.work.flow.entity.UserEntity;
import com.work.flow.exception.DuplicateUserException;
import com.work.flow.exception.EntityNotFoundException;
import com.work.flow.repository.users.JpaUserRepo;


public class UserService {
    private final JpaUserRepo userRepo;

    public UserService(JpaUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserEntity createUser(String username, String email) {
        Validator.emailValid(email);
        Validator.usernameValid(username);
        if (userRepo.findByEmail(email).isPresent()) {
            throw new DuplicateUserException(email);
        }
        return userRepo.save(new UserEntity(username, email));
    }

    public UserEntity login(String email) {
        Validator.emailValid(email);
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email : " + email + " not found"));
    }

}
