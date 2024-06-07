package com.security.security.exception.usuario;

import com.security.security.exception.AppSecurityException;

public class UsuarioSenhaInvalidaException extends AppSecurityException {
    public UsuarioSenhaInvalidaException() {
        super("Senha do usuario eh invalida.");
    }
}
