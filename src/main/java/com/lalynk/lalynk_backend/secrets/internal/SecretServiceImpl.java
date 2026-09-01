package com.lalynk.lalynk_backend.secrets.internal;
import com.lalynk.lalynk_backend.secrets.CreateSecretRequest;
import com.lalynk.lalynk_backend.secrets.ISecretService;
import com.lalynk.lalynk_backend.secrets.SecretDTO;
import com.lalynk.lalynk_backend.users.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public SecretDTO createSecret(CreateSecretRequest secretDTO) {
        boolean userExists = iuserService.doesExist(secretDTO.userId());
        if(!userExists) {
            throw new RuntimeException("User with id "+ secretDTO.userId()+ " does not exist.");
        }
        Secret secret = new Secret(secretDTO.userId(), secretDTO.expiresAt(), secretDTO.content(), secretTokenGenerator.generate());
        Secret saved = secretRepository.save(secret);
        return new SecretDTO(saved.getId(), saved.getCreatedAt(), saved.getExpiresAt(), saved.getContent(), saved.getPublicToken());
    }

    @Override
    public List<SecretDTO> getAllSecrets() {
        List<SecretDTO> secretDTOS = new ArrayList<>();
        List<Secret> secrets = secretRepository.findAll();
        for(Secret s: secrets) {
            secretDTOS.add(new SecretDTO(s.getId(),s.getCreatedAt(),s.getExpiresAt(),s.getContent(),s.getPublicToken()));
        }
        return secretDTOS;
    }


}
