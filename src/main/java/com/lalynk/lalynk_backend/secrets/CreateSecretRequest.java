package com.lalynk.lalynk_backend.secrets;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateSecretRequest(@Future Instant expiresAt, @Size(max = 10_000) @NotBlank String content) {
}
