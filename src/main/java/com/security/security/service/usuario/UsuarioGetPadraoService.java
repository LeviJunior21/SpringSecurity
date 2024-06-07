package com.security.security.service.usuario;

import com.security.security.dto.login.LoginUsuario;
import com.security.security.dto.usuario.UsuarioDTO;
import com.security.security.exception.usuario.UsuarioNaoExisteException;
import com.security.security.exception.usuario.UsuarioSenhaInvalidaException;
import com.security.security.model.Usuario;
import com.security.security.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioGetPadraoService implements UsuarioGetService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> get(LoginUsuario loginUsuario) {
        if (loginUsuario == null) {
            return usuarioRepository.findAll().stream().map(usuario -> modelMapper.map(usuario, UsuarioDTO.class)).collect(Collectors.toList());
        }

        Usuario usuario = usuarioRepository.buscarPorNome(loginUsuario.getNome()).orElseThrow(UsuarioNaoExisteException::new);
        if (!passwordEncoder.matches(loginUsuario.getSenha(), usuario.getSenha())) {
            throw new UsuarioSenhaInvalidaException();
        }

        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        List<UsuarioDTO> usuarioDTOS = Collections.singletonList(usuarioDTO);
        return usuarioDTOS;
    }
}
