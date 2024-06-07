package com.security.security.exception.usuario;

import com.security.security.exception.AppSecurityException;

public class UsuarioNaoEstaHabilitadoException extends AppSecurityException {
    public UsuarioNaoEstaHabilitadoException() {
        super("O usuário está desabilitado");
    }
}
