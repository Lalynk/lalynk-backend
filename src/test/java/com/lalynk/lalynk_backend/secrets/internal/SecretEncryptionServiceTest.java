package com.lalynk.lalynk_backend.secrets.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SecretEncryptionServiceTest {

    private SecretEncryptionService secretEncryptionService;

    @BeforeEach
    void setup() {
        String key = "7neitSICUtZppFrUnN3b42rbgYV8PtAe0vOZFfqoat4=";

        secretEncryptionService = new SecretEncryptionService(key);
    }

    @Test
    void shouldEncryptAndDecryptSecret() {
        String plainText = "hejhej";

        String encryptedMessage = secretEncryptionService.encrypt(plainText);
        String decryptedMessage = secretEncryptionService.decrypt(encryptedMessage);

        assertEquals(plainText, decryptedMessage);
    }

    @Test
    void shouldProduceDifferentCipherTextForSamePlainText() {
        String plainText = "hejhej";

        String decryptedMessage1 = secretEncryptionService.encrypt(plainText);
        String decryptedMessage2 = secretEncryptionService.encrypt(plainText);

        assertNotEquals(decryptedMessage1, decryptedMessage2);

    }

}
