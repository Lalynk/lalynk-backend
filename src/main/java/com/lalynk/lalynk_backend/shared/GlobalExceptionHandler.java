package com.lalynk.lalynk_backend.shared;


import com.lalynk.lalynk_backend.secrets.InvalidEncryptionKeyException;
import com.lalynk.lalynk_backend.secrets.InvalidExpirationException;
import com.lalynk.lalynk_backend.secrets.SecretLimitExceededException;
import com.lalynk.lalynk_backend.secrets.SecretNotFoundException;
import com.lalynk.lalynk_backend.users.UserAlreadyExistsException;

import com.lalynk.lalynk_backend.users.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists() {
        return new ResponseEntity<>(new ErrorResponse("User already exists"), HttpStatus.CONFLICT);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound() {
        return new ResponseEntity<>(new ErrorResponse("User not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecretNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSecretNotFound() {
        return new ResponseEntity<>(new ErrorResponse("Secret not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecretLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleSecretLimitExceeded() {
        return new ResponseEntity<>(new ErrorResponse("Maximum number of active secrets reached"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidExpirationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidExpiration() {
        return new ResponseEntity<>(new ErrorResponse("Secret expiration cannot exceed 30 days"), HttpStatus.BAD_REQUEST);
    }


}
