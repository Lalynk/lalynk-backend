package com.lalynk.lalynk_backend.users.internal;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String password;

    @Column(name = "created_at")
    private Instant createdAt;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.createdAt = Instant.now();
    }

    protected User(){}

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
