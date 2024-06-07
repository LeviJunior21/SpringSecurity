package com.security.security.service.usuario;

import com.security.security.dto.usuario.UsuarioDTO;
import com.security.security.dto.usuario.UsuarioPostRequestDTO;

@FunctionalInterface
public interface UsuarioCreateService {
    UsuarioDTO criar(UsuarioPostRequestDTO usuarioPostRequestDTO);
}
