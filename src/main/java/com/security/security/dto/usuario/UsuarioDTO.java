package com.security.security.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    @JsonProperty("id")
    Long id;

    @JsonProperty("nome")
    String nome;
}
