package com.lalynk.lalynk_backend.secrets;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record CreateSecretRequest(@Future Instant expiresAt,@NotBlank String content) {
}
