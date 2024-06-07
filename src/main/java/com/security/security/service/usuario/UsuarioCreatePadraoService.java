package com.security.security.service.usuario;

import com.security.security.dto.usuario.UsuarioDTO;
import com.security.security.dto.usuario.UsuarioPostRequestDTO;
import com.security.security.exception.usuario.UsuarioJaExisteException;
import com.security.security.model.Usuario;
import com.security.security.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioCreatePadraoService implements UsuarioCreateService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO criar(UsuarioPostRequestDTO usuarioPostRequestDTO) {
        Optional<Usuario> usuarioRecuperado = usuarioRepository.buscarPorNome(usuarioPostRequestDTO.getNome());
        if (usuarioRecuperado.isPresent()) {
            throw new UsuarioJaExisteException();
        }

        String passwordCrypto = passwordEncoder.encode(usuarioPostRequestDTO.getSenha());
        usuarioPostRequestDTO.setSenha(passwordCrypto);

        Usuario usuario = modelMapper.map(usuarioPostRequestDTO, Usuario.class);
        usuario = usuarioRepository.save(usuario);

        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        return usuarioDTO;
    }
}
