package com.security.security.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.security.exception.AppSecurityException;
import com.security.security.exception.CustomErrorType;
import com.security.security.exception.usuario.UsuarioNaoEstaHabilitadoException;
import com.security.security.exception.usuario.UsuarioNaoExisteException;
import com.security.security.model.Usuario;
import com.security.security.repositories.UsuarioRepository;
import com.security.security.service.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, UsuarioNaoEstaHabilitadoException {
        String token = this.recoverToken(request);
        String uri = request.getRequestURI();

        if (token != null) {
            String nome = this.jwtService.validarToken(token);
            Usuario usuario = usuarioRepository.buscarPorNome(nome).orElseThrow(UsuarioNaoExisteException::new);

            try {
                if (!usuario.getHabilitado()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    throw new UsuarioNaoEstaHabilitadoException();
                } else if (uri != null && uri.startsWith("/v1/usuarios/deletarAll") && !usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(authority -> authority.equals("ROLE_ADMIN"))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    throw new UsuarioNaoEstaHabilitadoException("O usuario nao é um administrador");
                }
            } catch (AppSecurityException e) {
                CustomErrorType customErrorType = new CustomErrorType(List.of(e.getMessage()));
                response.getWriter().write(objectMapper.writeValueAsString(customErrorType));
                return;
            }

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest httpServletRequest) {
        var authHeader = httpServletRequest.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
