package com.security.security.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Dados publicos do usuário")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    @Schema(description = "ID do usuário", defaultValue = "ID do usuário", example = "10")
    @JsonProperty("id")
    @NotBlank(message = "Id invalido")
    Long id;

    @Schema(description = "Nome do usuário", defaultValue = "Nome do usuário", example = "João")
    @JsonProperty("nome")
    @NotBlank(message = "Nome invalido")
    String nome;
}
