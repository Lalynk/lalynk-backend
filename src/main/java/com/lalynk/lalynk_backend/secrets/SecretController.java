package com.lalynk.lalynk_backend.secrets;


import com.lalynk.lalynk_backend.secrets.internal.Secret;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/secrets")
public class SecretController {

    private ISecretService iSecretService;

    public SecretController(ISecretService iSecretService) {
        this.iSecretService = iSecretService;
    }

    @PostMapping("")
    public ResponseEntity<SecretDTO> createSecret(@RequestBody CreateSecretRequest secretRequest, Authentication authentication) {
        return new ResponseEntity<>(iSecretService.createSecret(secretRequest, authentication), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<SecretDTO>> getMySecrets(Authentication authentication) {
        return new ResponseEntity<>(iSecretService.getMySecrets(authentication), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretDTO> getSecretById(Authentication authentication, @PathVariable UUID id) {
        return new ResponseEntity<>(iSecretService.getSecretById(authentication, id), HttpStatus.OK);

    }

    @PostMapping("/{id}/revoke")
    public ResponseEntity<Void> revokeSecret(Authentication authentication, @PathVariable UUID id) {
        iSecretService.revokeSecret(authentication, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/public/{publicToken}")
    public ResponseEntity<PublicSecretDTO> openSecret(@PathVariable String publicToken) {
        return new ResponseEntity<>(iSecretService.openSecret(publicToken), HttpStatus.OK);
    }



}
