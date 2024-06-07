package com.security.security.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUsuario {
    @JsonProperty("nome")
    String nome;

    @JsonProperty("senha")
    String senha;
}
