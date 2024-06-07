package com.security.security.service.usuario;

import com.security.security.dto.login.LoginUsuario;
import com.security.security.dto.usuario.UsuarioDTO;
import java.util.List;

@FunctionalInterface
public interface UsuarioGetService {
    List<UsuarioDTO> get(LoginUsuario loginUsuario);
}
