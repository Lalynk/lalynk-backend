package com.lalynk.lalynk_backend.secrets;

import java.time.Instant;
import java.util.UUID;

public record SecretSummaryDTO(UUID id, Instant createdAt, Instant expiresAt, Instant consumedAt, Instant revokedAt, String publicToken) {
}
