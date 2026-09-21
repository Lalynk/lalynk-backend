package com.lalynk.lalynk_backend.secrets;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/secrets")
public class SecretController {

    private final ISecretService iSecretService;

    public SecretController(ISecretService iSecretService) {
        this.iSecretService = iSecretService;
    }

    @PostMapping("")
    public ResponseEntity<SecretDTO> createSecret(@Valid @RequestBody CreateSecretRequest secretRequest, Authentication authentication) {
        return new ResponseEntity<>(iSecretService.createSecret(secretRequest, authentication.getName()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<List<SecretSummaryDTO>> getMySecrets(Authentication authentication) {
        return new ResponseEntity<>(iSecretService.getMySecrets(authentication.getName()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<SecretDTO> getSecretById(Authentication authentication, @PathVariable UUID id) {
        return new ResponseEntity<>(iSecretService.getSecretById(authentication.getName(), id), HttpStatus.OK);

    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/revoke")
    public ResponseEntity<Void> revokeSecret(Authentication authentication, @PathVariable UUID id) {
        iSecretService.revokeSecret(authentication.getName(), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/public/{publicToken}")
    public ResponseEntity<PublicSecretDTO> openSecret(@PathVariable String publicToken) {
        return new ResponseEntity<>(iSecretService.openSecret(publicToken), HttpStatus.OK);
    }

}
