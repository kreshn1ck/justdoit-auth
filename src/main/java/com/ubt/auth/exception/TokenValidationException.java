package com.ubt.auth.exception;

public class TokenValidationException extends Exception {
    public TokenValidationException(String msg) {
        super(msg);
    }
}
