package com.security.security.exception.usuario;

import com.security.security.exception.AppSecurityException;

public class UsuarioNaoExisteException extends AppSecurityException {
    public UsuarioNaoExisteException() {
        super("Usuario não existente.");
    }
}
