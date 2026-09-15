package com.lalynk.lalynk_backend.secrets.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SecretRepository extends JpaRepository<Secret, UUID> {
    List<Secret> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Secret> findByPublicToken(String publicToken);

    @Modifying
    @Query("""
    UPDATE Secret s
    SET s.consumedAt = CURRENT_TIMESTAMP
    WHERE s.publicToken = :publicToken
      AND s.consumedAt IS NULL
      AND s.revokedAt IS NULL
      AND (s.expiresAt IS NULL OR s.expiresAt > CURRENT_TIMESTAMP)
""")
    int consumeIfAvailable(@Param("publicToken") String publicToken);


}
