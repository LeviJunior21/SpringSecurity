package com.security.security.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.security.security.model.UsuarioAuthenticated;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {
    String secretKey = "chave-secreta";

    public String gerarToken(UsuarioAuthenticated usuarioAuthenticated) {
        return JWT.create()
                .withIssuer("usuarios-auth")
                .withSubject(usuarioAuthenticated.getUsername())
                .withClaim("id", usuarioAuthenticated.getId())
                .withExpiresAt(LocalDateTime.now()
                        .plusMinutes(30)
                        .toInstant(ZoneOffset.of("-03:00"))
                ).sign(Algorithm.HMAC256(secretKey));
    }

    public String validarToken(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .withIssuer("usuarios-auth")
                .build().verify(token).getSubject();
    }
}
