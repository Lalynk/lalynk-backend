package com.lalynk.lalynk_backend.secrets.internal;

import com.lalynk.lalynk_backend.secrets.PublicSecretDTO;
import com.lalynk.lalynk_backend.secrets.SecretNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SecretServiceTest {

    @Mock
    private SecretRepository secretRepository;

    private SecretServiceImpl secretService;
    @Mock
    private UserServiceImpl userService;



    @Test
    void ShouldOpenValidSecret() {
        UUID userId = UUID.randomUUID();
        String publicToken = "abc123";
        Secret secret = new Secret(userId, Instant.now().plusSeconds(5000), "test-secret", publicToken);

        when(secretRepository.findByPublicToken(publicToken)).thenReturn(Optional.of(secret));

        PublicSecretDTO result = secretService.openSecret(publicToken);

        assertEquals("test-secret", result.content());
        assertNotNull(secret.getConsumedAt());
    }




    @Test
    void ShouldNotAllowOtherUserToGetSecret() {
        //user is created
        String auth0SubjectUser = "1111";
        UUID userId = UUID.randomUUID();
        //this user has a secret
        UUID secretId = UUID.randomUUID();
        String publicToken = "abc123";
        Secret s = new Secret(userId, Instant.now().plusSeconds(5000), "himligt meddelande", publicToken);
        when(secretRepository.findById(secretId)).thenReturn(Optional.of(s));
        //other user wants to access this secret
        String auth0SubjectOtherUser = "2222";
        UUID otherUserId = UUID.randomUUID();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(auth0SubjectOtherUser);
        when(userService.findUserIdBySubject(auth0SubjectOtherUser)).thenReturn(otherUserId);
        assertThrows(SecretNotFoundException.class, () -> secretService.getSecretById(authentication.getName(), secretId));
    }

    //    @Override
    //    public PublicSecretDTO openSecret(String publicToken) {
    //        Secret secret = secretRepository.findByPublicToken(publicToken).orElseThrow(() -> new SecretNotFoundException());
    //        if(secret.getRevokedAt() != null || (secret.getExpiresAt() != null && secret.getExpiresAt().isBefore(Instant.now())) || secret.getConsumedAt() != null) throw new SecretNotFoundException();
    //        secret.consume();
    //        secretRepository.save(secret);
    //        return new PublicSecretDTO(secret.getContent());
    //    }



    @Test
    void shouldNotOpenExpiredSecret() {
        UUID userId = UUID.randomUUID();
        String publicToken = "abc123";
        Secret secret = new Secret(userId, Instant.now().minusSeconds(60), "Test message", publicToken);
        when(secretRepository.findByPublicToken(publicToken)).thenReturn(Optional.of(secret));
        assertThrows(SecretNotFoundException.class, () -> secretService.openSecret(publicToken));

    }


    //ShouldNotOpenRevokedSecret
    //ShouldNotOpenNonExistingSecret
    //shouldNotAllowOtherUserToRevokeSecret





}
