package com.work.flow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class UserEntity {

    @Id
    @Column
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    public UserEntity() {
    }

    public UserEntity(String username, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
