package com.security.security.service.autenticacao;

import com.security.security.dto.login.AuthenticationRequestDTO;
import com.security.security.dto.login.LoginResponseDTO;
import com.security.security.exception.usuario.UsuarioNaoExisteException;
import com.security.security.exception.usuario.UsuarioSenhaInvalidaException;
import com.security.security.model.Usuario;
import com.security.security.model.UsuarioAuthenticated;
import com.security.security.repositories.UsuarioRepository;
import com.security.security.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationPadraoService implements AuthenticationService {
    @Autowired
    JwtService jwtService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    public LoginResponseDTO autenticar(AuthenticationRequestDTO authenticationRequestDTO) {
        Usuario usuarioResponse = usuarioRepository.buscarPorNome(authenticationRequestDTO.getNome()).orElseThrow(UsuarioNaoExisteException::new);
        if (!passwordEncoder.matches(authenticationRequestDTO.getSenha(), usuarioResponse.getSenha())) {
            throw new UsuarioSenhaInvalidaException();
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getNome(), authenticationRequestDTO.getSenha());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UsuarioAuthenticated usuarioAuthenticated = new UsuarioAuthenticated();
        usuarioAuthenticated.setNome(userDetails.getUsername());
        usuarioAuthenticated.setSenha(userDetails.getPassword());

        String token = jwtService.gerarToken(usuarioAuthenticated);

        return LoginResponseDTO.builder()
                .token(token)
                .build();

    }
}
