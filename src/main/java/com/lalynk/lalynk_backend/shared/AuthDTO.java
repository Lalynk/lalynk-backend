package com.lalynk.lalynk_backend.shared;

public record AuthDTO(boolean authenticated, String subject, String email) {
}
