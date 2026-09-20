package com.work.flow.entity;

import java.util.UUID;

public record TaskDto(
        UUID taskId,
        String name,
        String status
){}
