package com.lalynk.lalynk_backend.secrets.internal;

import com.lalynk.lalynk_backend.secrets.CreateSecretRequest;
import com.lalynk.lalynk_backend.secrets.PublicSecretDTO;
import com.lalynk.lalynk_backend.secrets.SecretDTO;
import com.lalynk.lalynk_backend.secrets.SecretNotFoundException;
import com.lalynk.lalynk_backend.users.IUserService;
import com.lalynk.lalynk_backend.users.internal.User;
import com.lalynk.lalynk_backend.users.internal.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

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

        //given
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

        //when
        SecretDTO result = secretService.createSecret(secretRequest, auth0Subject);

        //then

        assertEquals(expiresAt, result.expiresAt());
        assertEquals(publicToken, result.publicToken());
        assertEquals(content, result.content());

        verify(secretEncryptionService).encrypt(content);

        verify(secretRepository).save(any(Secret.class));
    }



    @Test
    void ShouldNotAllowOtherUserToGetSecret() {

    }



    @Test
    void shouldNotOpenExpiredSecret() {

    }


}
