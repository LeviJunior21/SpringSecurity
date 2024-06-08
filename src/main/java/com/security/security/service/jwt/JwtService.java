package com.security.security.service.jwt;

import com.security.security.model.UsuarioAuthenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class JwtService {
    String issuer = "usuarios-auth";
    @Autowired
    JwtEncoder jwtEncoder;
    @Autowired
    JwtDecoder jwtDecoder;

    public String gerarToken(UsuarioAuthenticated usuarioAuthenticated) {
        Instant now = Instant.now();
        long exp = 3600L;
        Consumer<Map<String, Object>> claimsConsumer = (claims) -> {
            claims.put("authorities", usuarioAuthenticated.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        };

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(exp))
                .subject(usuarioAuthenticated.getNome())
                .claims(claimsConsumer)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    public String validarToken(String token) {
        var validToken = token.replace("Bearer ", "");
        Jwt jwt = jwtDecoder.decode(validToken);
        return jwt.getSubject();
    }
}
