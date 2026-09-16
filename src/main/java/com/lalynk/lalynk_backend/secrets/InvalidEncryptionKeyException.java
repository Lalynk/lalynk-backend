package com.lalynk.lalynk_backend.secrets;

public class InvalidEncryptionKeyException extends RuntimeException{

    public InvalidEncryptionKeyException(String msg) {
        super(msg);
    }

}
