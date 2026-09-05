package com.lalynk.lalynk_backend.shared;


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


}
