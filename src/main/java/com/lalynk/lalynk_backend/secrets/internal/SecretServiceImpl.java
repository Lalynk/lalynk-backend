package com.lalynk.lalynk_backend.secrets.internal;
import com.lalynk.lalynk_backend.secrets.*;
import com.lalynk.lalynk_backend.users.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SecretServiceImpl implements ISecretService {


    private SecretRepository secretRepository;
    private IUserService iuserService;
    private SecretTokenGenerator secretTokenGenerator;

    public SecretServiceImpl(SecretRepository secretRepository, IUserService iUserService, SecretTokenGenerator secretTokenGenerator){
        this.iuserService = iUserService;
        this.secretRepository = secretRepository;
        this.secretTokenGenerator = secretTokenGenerator;
    }

    @Override
    public SecretDTO createSecret(CreateSecretRequest secretDTO, Authentication authentication) {
        String auth0Subject = authentication.getName();
        UUID userId = iuserService.findUserIdBySubject(auth0Subject);
        Secret secret = new Secret(userId, secretDTO.expiresAt(), secretDTO.content(), secretTokenGenerator.generate());
        Secret saved = secretRepository.save(secret);
        return new SecretDTO(saved.getId(), saved.getCreatedAt(), saved.getExpiresAt(), saved.getConsumedAt(), saved.getRevokedAt(), saved.getContent(), saved.getPublicToken());
    }

    @Override
    public List<SecretDTO> getMySecrets(Authentication authentication) {
        List<SecretDTO> secretDTOS = new ArrayList<>();
        String auth0Subject = authentication.getName();
        UUID userId = iuserService.findUserIdBySubject(auth0Subject);
        List<Secret> secrets = secretRepository.findByUserIdOrderByCreatedAtDesc(userId);
        for(Secret s: secrets) {
            secretDTOS.add(new SecretDTO(s.getId(), s.getCreatedAt(),s.getExpiresAt(), s.getConsumedAt(), s.getRevokedAt(), s.getContent(),s.getPublicToken()));
        }
        return secretDTOS;
    }

    @Override
    public SecretDTO getSecretById(Authentication authentication, UUID secretId) {
        Secret secret = secretRepository.findById(secretId).orElseThrow(()-> new SecretNotFoundException());
        String auth0Subject = authentication.getName();
        UUID userId = iuserService.findUserIdBySubject(auth0Subject);
        if(!secret.getUserId().equals(userId)) throw new SecretNotFoundException();
        return new SecretDTO(secret.getId(),secret.getCreatedAt(),secret.getExpiresAt(), secret.getConsumedAt(), secret.getRevokedAt(), secret.getContent(),secret.getPublicToken());

    }

    @Transactional
    @Override
    public void revokeSecret(Authentication authentication, UUID secretId) {
        String auth0Subject = authentication.getName();
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

        return new PublicSecretDTO(secret.getContent());
    }


}
