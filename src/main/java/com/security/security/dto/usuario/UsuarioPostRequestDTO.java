package com.security.security.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPostRequestDTO {
    @JsonProperty("nome")
    @NotBlank(message = "Nome invalido")
    String nome;

    @JsonProperty("senha")
    @NotBlank(message = "Senha invalida")
    String senha;
}
