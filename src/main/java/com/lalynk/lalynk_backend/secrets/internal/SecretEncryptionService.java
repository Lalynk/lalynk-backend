package com.lalynk.lalynk_backend.secrets.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.AesGcmBytesEncryptor;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SecretEncryptionService {

    private final AesGcmBytesEncryptor encryptor;

    public SecretEncryptionService(@Value("${secret.encryption.key}") String key) {
        byte[] keyBytes = Base64.getDecoder().decode(key);

        if(keyBytes.length != 32) throw new IllegalArgumentException("Secret encryption key must be 32 bytes");

        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        this.encryptor = AesGcmBytesEncryptor.withSecretKey(secretKey).build();
    }
    public String encrypt(String plainText) {
        byte[] plaintextBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = encryptor.encrypt(plaintextBytes);
        return Base64.getUrlEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encrypted) {
        byte[] encryptedBytes = Base64.getUrlDecoder().decode(encrypted);

        byte[] plaintextBytes = encryptor.decrypt(encryptedBytes);

        return new String(plaintextBytes, StandardCharsets.UTF_8);
    }


}
