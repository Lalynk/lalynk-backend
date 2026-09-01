package com.lalynk.lalynk_backend.secrets;

import java.time.Instant;
import java.util.UUID;

public record SecretDTO(UUID id, Instant createdAt, Instant expiresAt, String content, String publicToken) {
}
