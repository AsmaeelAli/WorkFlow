package com.work.flow.repository.tasks;

import com.work.flow.entity.TaskEntity;
import com.work.flow.repository.jparepo.JpaRepository;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JpaTaskRepo extends JpaRepository<TaskEntity, UUID> implements TaskRepository {

    private final EntityManager entityManager;

    public JpaTaskRepo(EntityManager entityManager) {
        super(entityManager, TaskEntity.class);
        this.entityManager = entityManager;
    }

    public List<TaskEntity> findAllByUserId(UUID userId) {
        String jpql = "SELECT t FROM TaskEntity t WHERE t.user.id = :userId";
        return entityManager.createQuery(jpql, TaskEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
