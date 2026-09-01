package com.lalynk.lalynk_backend.secrets.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecretRepository extends JpaRepository<Secret, UUID> {
}
