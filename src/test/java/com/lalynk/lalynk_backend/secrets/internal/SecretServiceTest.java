package com.lalynk.lalynk_backend.secrets.internal;

import com.lalynk.lalynk_backend.secrets.CreateSecretRequest;
import com.lalynk.lalynk_backend.secrets.PublicSecretDTO;
import com.lalynk.lalynk_backend.secrets.SecretDTO;
import com.lalynk.lalynk_backend.secrets.SecretNotFoundException;
import com.lalynk.lalynk_backend.users.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecretServiceTest {

    @Mock
    private SecretRepository secretRepository;

    private SecretServiceImpl secretService;

    @Mock
    private IUserService iUserService;

    @Mock
    private SecretTokenGenerator secretTokenGenerator;

    @Mock
    private SecretEncryptionService secretEncryptionService;

    @BeforeEach
    void setup() {
        secretService = new SecretServiceImpl(secretRepository, iUserService, secretTokenGenerator, secretEncryptionService);
    }

    @Test
    void ShouldCreateSecret() {

        UUID userId = UUID.randomUUID();
        String auth0Subject = "abc123";
        String content = "testingtesting";
        String encryptedContent = "###";
        String publicToken = "121212";
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        CreateSecretRequest secretRequest = new CreateSecretRequest(expiresAt, content);

        when(iUserService.findUserIdBySubject(auth0Subject)).thenReturn(userId);
        when(secretRepository.countActiveSecrets(userId)).thenReturn(1L);
        when(secretEncryptionService.encrypt(secretRequest.content())).thenReturn(encryptedContent);
        when(secretTokenGenerator.generate()).thenReturn(publicToken);
        Secret saved = new Secret(userId,secretRequest.expiresAt(), encryptedContent, publicToken);
        when(secretRepository.save(any(Secret.class))).thenReturn(saved);

        SecretDTO result = secretService.createSecret(secretRequest, auth0Subject);

        assertEquals(expiresAt, result.expiresAt());
        assertEquals(publicToken, result.publicToken());
        assertEquals(content, result.content());
        verify(secretEncryptionService).encrypt(content);
        verify(secretRepository).save(any(Secret.class));
    }

    @Test
    void ShouldNotAllowOtherUserToGetSecret() {
        UUID userIdCorrect = UUID.randomUUID();
        UUID secretId = UUID.randomUUID();
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        String encryptedContent = "###";
        String publicToken = "121212";
        Secret secret = new Secret(userIdCorrect,expiresAt, encryptedContent, publicToken);

        when(secretRepository.findById(secretId)).thenReturn(Optional.of(secret));
        String auth0Subject = "abc|123";
        UUID userIdFalse = UUID.randomUUID();
        when(iUserService.findUserIdBySubject(auth0Subject)).thenReturn(userIdFalse);

        assertThrows(SecretNotFoundException.class, () -> secretService.getSecretById(auth0Subject, secretId));
    }

    @Test
    void shouldOpenValidSecret() {
        String publicToken = "121212";
        UUID userId = UUID.randomUUID();
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        String encryptedContent = "###";

        when(secretRepository.consumeIfAvailable(publicToken)).thenReturn(1);
        Secret secret = new Secret(userId,expiresAt, encryptedContent, publicToken);
        when(secretRepository.findByPublicToken(publicToken)).thenReturn(Optional.of(secret));
        String decryptedContent = "hejhej";
        when(secretEncryptionService.decrypt(secret.getContent())).thenReturn(decryptedContent);

        PublicSecretDTO publicSecretDTO = secretService.openSecret(publicToken);
        assertEquals(decryptedContent, publicSecretDTO.content());
    }


    @Test
    void shouldNotOpenAlreadyConsumedSecret() {
        String publicToken = "121212";
        when(secretRepository.consumeIfAvailable(publicToken)).thenReturn(0);

        assertThrows(SecretNotFoundException.class, ()-> secretService.openSecret(publicToken));
    }

    @Test
    void shouldNotOpenExpiredSecret() {
        String publicToken = "121212";
        when(secretRepository.consumeIfAvailable(publicToken)).thenReturn(0);

        assertThrows(SecretNotFoundException.class, ()-> secretService.openSecret(publicToken));
    }


    @Test
    void shouldRevokeSecret() {
        UUID userId = UUID.randomUUID();
        String auth0Subject = "abc|123";
        UUID secretId = UUID.randomUUID();

        when(iUserService.findUserIdBySubject(auth0Subject)).thenReturn(userId);
        when(secretRepository.revokeIfAvailable(secretId, userId)).thenReturn(1);

        assertDoesNotThrow(()-> secretService.revokeSecret(auth0Subject, secretId));
    }

    @Test
    void shouldNotRevokeSecret() {
        UUID userId = UUID.randomUUID();
        String auth0Subject = "abc|123";
        UUID secretId = UUID.randomUUID();

        when(iUserService.findUserIdBySubject(auth0Subject)).thenReturn(userId);
        when(secretRepository.revokeIfAvailable(secretId, userId)).thenReturn(0);

        assertThrows(SecretNotFoundException.class,()-> secretService.revokeSecret(auth0Subject, secretId));

    }
}
