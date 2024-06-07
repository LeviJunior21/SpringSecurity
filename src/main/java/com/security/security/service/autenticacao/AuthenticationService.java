package com.security.security.service.autenticacao;

import com.security.security.dto.login.AuthenticationRequestDTO;
import com.security.security.dto.login.LoginResponseDTO;

@FunctionalInterface
public interface AuthenticationService {
    LoginResponseDTO autenticar(AuthenticationRequestDTO authenticationRequestDTO);
}
