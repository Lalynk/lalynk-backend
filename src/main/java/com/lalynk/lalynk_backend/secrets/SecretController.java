package com.lalynk.lalynk_backend.secrets;


import com.lalynk.lalynk_backend.secrets.internal.Secret;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secrets")
public class SecretController {

    private ISecretService iSecretService;

    public SecretController(ISecretService iSecretService) {
        this.iSecretService = iSecretService;
    }

    @PostMapping("")
    public ResponseEntity<SecretDTO> createSecret(@RequestBody CreateSecretRequest secretRequest) {
        return new ResponseEntity<>(iSecretService.createSecret(secretRequest), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<SecretDTO>> getAllSecrets() {
        return new ResponseEntity<>(iSecretService.getAllSecrets(), HttpStatus.CREATED);
    }



}
