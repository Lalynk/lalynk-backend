package com.lalynk.lalynk_backend.shared;


import com.lalynk.lalynk_backend.users.UserAlreadyExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException() {
        return new ResponseEntity<>(new ErrorResponse("User already exists"), HttpStatus.CONFLICT);

    }


}
