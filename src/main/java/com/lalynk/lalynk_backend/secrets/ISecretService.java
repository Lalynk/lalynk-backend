package com.lalynk.lalynk_backend.secrets;
import java.util.List;
import java.util.UUID;

public interface ISecretService {
    SecretDTO createSecret(CreateSecretRequest request, String auth0Subject);
    List<SecretSummaryDTO> getMySecrets(String auth0Subject);
    SecretDTO getSecretById(String auth0Subject, UUID secretId);
    SecretSummaryDTO revokeSecret(String auth0Subject, UUID secretId);
    PublicSecretDTO openSecret(String publicToken);
}
