package com.lalynk.lalynk_backend.secrets;

import java.util.List;

public interface ISecretService {
    SecretDTO createSecret(CreateSecretRequest request);
    List<SecretDTO> getAllSecrets();
}
