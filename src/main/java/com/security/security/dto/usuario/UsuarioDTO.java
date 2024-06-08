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
public class UsuarioDTO {
    @JsonProperty("id")
    @NotBlank(message = "Id invalido")
    Long id;

    @JsonProperty("nome")
    @NotBlank(message = "Nome invalido")
    String nome;
}
