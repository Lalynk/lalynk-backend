package com.lalynk.lalynk_backend.secrets;

import java.time.Instant;
import java.util.UUID;

public record CreateSecretRequest(UUID userId, Instant expiresAt, String content) {
}
