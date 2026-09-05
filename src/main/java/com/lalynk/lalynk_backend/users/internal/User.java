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

    @Column(name = "auth0_subject", unique = true, nullable = false)
    private String auth0Subject;

    @Column(name = "created_at")
    private Instant createdAt;

    public User(String email, String auth0Subject) {
        this.email = email;
        this.auth0Subject = auth0Subject;
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

    public String getAuth0Subject() {
        return auth0Subject;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
