package com.security.security.exception;

public class AppSecurityException extends RuntimeException {
    public AppSecurityException() {
        super("Erro de validações encontrados");
    }

    public AppSecurityException(String message) {
        super(message);
    }
}
