package com.lalynk.lalynk_backend.secrets;

import com.lalynk.lalynk_backend.users.UserDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ISecretService {
    SecretDTO createSecret(CreateSecretRequest request);
    List<SecretDTO> getAllSecrets();
}
