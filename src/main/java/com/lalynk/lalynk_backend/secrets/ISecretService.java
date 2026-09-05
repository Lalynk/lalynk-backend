package com.lalynk.lalynk_backend.secrets;

import com.lalynk.lalynk_backend.users.UserDTO;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface ISecretService {
    SecretDTO createSecret(CreateSecretRequest request, Authentication authentication);
    List<SecretDTO> getMySecrets(Authentication authentication);
    SecretDTO getSecretById(Authentication authentication, UUID secretId);
    void revokeSecret(Authentication authentication, UUID secretId);
    PublicSecretDTO openSecret(String publicToken);
}
