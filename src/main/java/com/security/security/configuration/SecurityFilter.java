package com.security.security.configuration;

import com.security.security.exception.usuario.UsuarioNaoEstaHabilitadoException;
import com.security.security.exception.usuario.UsuarioNaoExisteException;
import com.security.security.model.Usuario;
import com.security.security.model.UsuarioAuthenticated;
import com.security.security.repositories.UsuarioRepository;
import com.security.security.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.recoverToken(request);

        if (token != null) {
            String nome = this.jwtService.validarToken(token);
            Usuario usuario = usuarioRepository.buscarPorNome(nome).orElseThrow(UsuarioNaoExisteException::new);
            UsuarioAuthenticated usuarioAuthenticated = modelMapper.map(usuario, UsuarioAuthenticated.class);
            if (!usuarioAuthenticated.getHabilitado()) {
                throw new UsuarioNaoEstaHabilitadoException();
            }

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuarioAuthenticated.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest httpServletRequest) {
        var authHeader = httpServletRequest.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "").substring(7);
    }
}
