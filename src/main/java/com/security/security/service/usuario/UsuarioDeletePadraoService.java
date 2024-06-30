package com.security.security.service.usuario;

import com.security.security.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDeletePadraoService implements UsuarioDeleteService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public void delete(Long idUsuario) {
        if (idUsuario != null && idUsuario > 0) {
            usuarioRepository.deleteById(idUsuario);
        } else if (idUsuario == null){
            usuarioRepository.deleteAll();
        }
    }
}
