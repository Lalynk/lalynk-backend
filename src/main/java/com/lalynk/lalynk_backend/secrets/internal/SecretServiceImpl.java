package com.lalynk.lalynk_backend.secrets.internal;
import com.lalynk.lalynk_backend.secrets.*;
import com.lalynk.lalynk_backend.users.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SecretServiceImpl implements ISecretService {


    private final SecretRepository secretRepository;
    private final IUserService iuserService;
    private final SecretTokenGenerator secretTokenGenerator;
    private final SecretEncryptionService secretEncryptionService;

    private static final long DEFAULT_EXPIRATION_DAYS = 7;
    private static final long MAX_EXPIRATION_DAYS = 30;
    private static final long MAX_ACTIVE_SECRETS = 100;

    public SecretServiceImpl(SecretRepository secretRepository, IUserService iUserService, SecretTokenGenerator secretTokenGenerator, SecretEncryptionService secretEncryptionService){
        this.iuserService = iUserService;
        this.secretRepository = secretRepository;
        this.secretTokenGenerator = secretTokenGenerator;
        this.secretEncryptionService = secretEncryptionService;
    }

    @Override
    public SecretDTO createSecret(CreateSecretRequest secretDTO, String auth0Subject) {

        Instant expiresAt = secretDTO.expiresAt();
        if(expiresAt == null) {
            expiresAt = Instant.now().plus(DEFAULT_EXPIRATION_DAYS, ChronoUnit.DAYS);
        }

        if(expiresAt.isAfter(Instant.now().plus(MAX_EXPIRATION_DAYS, ChronoUnit.DAYS))) {
            throw new InvalidExpirationException();
        }

        UUID userId = iuserService.findUserIdBySubject(auth0Subject);

        long activeSecrets = secretRepository.countActiveSecrets(userId);
        if(activeSecrets>= MAX_ACTIVE_SECRETS) {
            throw new SecretLimitExceededException();
        }

        String encryptedContent = secretEncryptionService.encrypt(secretDTO.content());
        Secret secret = new Secret(userId, expiresAt, encryptedContent, secretTokenGenerator.generate());
        Secret saved = secretRepository.save(secret);
        return new SecretDTO(saved.getId(), saved.getCreatedAt(), saved.getExpiresAt(), saved.getConsumedAt(), saved.getRevokedAt(), secretDTO.content(), saved.getPublicToken());
    }

    @Override
    public List<SecretSummaryDTO> getMySecrets(String auth0Subject) {
        List<SecretSummaryDTO> secretSummaryDTOS = new ArrayList<>();
        UUID userId = iuserService.findUserIdBySubject(auth0Subject);
        List<Secret> secrets = secretRepository.findByUserIdOrderByCreatedAtDesc(userId);
        for(Secret s: secrets) {
            secretSummaryDTOS.add(new SecretSummaryDTO(s.getId(), s.getCreatedAt(), s.getExpiresAt(), s.getConsumedAt(), s.getRevokedAt(), s.getPublicToken()));
        }
        return secretSummaryDTOS;
    }

    @Override
    public SecretDTO getSecretById(String auth0Subject, UUID secretId) {
        Secret secret = secretRepository.findById(secretId).orElseThrow(()-> new SecretNotFoundException());
        UUID userId = iuserService.findUserIdBySubject(auth0Subject);
        if(!secret.getUserId().equals(userId)) throw new SecretNotFoundException();
        String decryptedContent = secretEncryptionService.decrypt(secret.getContent());
        return new SecretDTO(secret.getId(),secret.getCreatedAt(),secret.getExpiresAt(), secret.getConsumedAt(), secret.getRevokedAt(), decryptedContent,secret.getPublicToken());

    }

    @Transactional
    @Override
    public void revokeSecret(String auth0Subject, UUID secretId) {
        UUID userId = iuserService.findUserIdBySubject(auth0Subject);
        int revoked = secretRepository.revokeIfAvailable(secretId, userId);
        if(revoked == 0) throw new SecretNotFoundException();
    }

    @Transactional
    @Override
    public PublicSecretDTO openSecret(String publicToken) {
        int consumed = secretRepository.consumeIfAvailable(publicToken);
        if(consumed==0) {
            throw new SecretNotFoundException();
        }
        Secret secret = secretRepository.findByPublicToken(publicToken).orElseThrow(() -> new SecretNotFoundException());
        String decryptedContent = secretEncryptionService.decrypt(secret.getContent());
        return new PublicSecretDTO(decryptedContent);
    }


}
