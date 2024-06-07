package com.security.security.service.autenticacao;

import com.security.security.exception.usuario.UsuarioNaoExisteException;
import com.security.security.model.Usuario;
import com.security.security.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDatailsService implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.buscarPorNome(username).orElseThrow(UsuarioNaoExisteException::new);
        return new org.springframework.security.core.userdetails.User(usuario.getNome(), usuario.getSenha(), new ArrayList<>());
    }
}
