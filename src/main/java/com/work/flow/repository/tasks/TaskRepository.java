package com.work.flow.repository.tasks;

import com.work.flow.entity.TaskEntity;
import com.work.flow.repository.jparepo.Repository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends Repository<TaskEntity, UUID> {
    List<TaskEntity> findAllByUserId(UUID userId);
}
