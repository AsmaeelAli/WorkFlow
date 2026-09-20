package com.work.flow.repository.users;


import com.work.flow.entity.UserEntity;
import com.work.flow.repository.jparepo.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
}
