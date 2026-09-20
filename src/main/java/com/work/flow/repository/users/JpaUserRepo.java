package com.work.flow.repository.users;

import com.work.flow.entity.UserEntity;
import com.work.flow.repository.jparepo.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

public class JpaUserRepo extends JpaRepository<UserEntity, UUID> implements UserRepository {

    private final EntityManager entityManager;

    public JpaUserRepo(EntityManager entityManager) {
        super(entityManager, UserEntity.class);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        String jpql = "SELECT u FROM UserEntity u WHERE u.email = :email";
        return entityManager.createQuery(jpql, UserEntity.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
}
