package com.lalynk.lalynk_backend.secrets.internal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SecretRepository extends JpaRepository<Secret, UUID> {
    List<Secret> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Secret> findByPublicToken(String publicToken);
}
