package com.security.security.exception.usuario;

import com.security.security.exception.AppSecurityException;

public class UsuarioJaExisteException extends AppSecurityException {
    public UsuarioJaExisteException() {
        super("Usuario ja eh existente.");
    }
}
