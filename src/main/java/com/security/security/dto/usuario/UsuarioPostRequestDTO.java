package com.security.security.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Usuário a ser criado.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPostRequestDTO {
    @Schema(description = "Nome do usuário a ser criado.", defaultValue = "Seu nome", example = "Seu nome")
    @JsonProperty("nome")
    @NotBlank(message = "Nome invalido")
    String nome;

    @Schema(description = "Senha do usuário a ser criado.", defaultValue = "Sua senha", example = "Seu nome")
    @JsonProperty("senha")
    @NotBlank(message = "Senha invalida")
    String senha;

    @Schema(description = "Papel do usuário a ser criado.", defaultValue = "Seu papel", example = "Seu nome")
    @JsonProperty("role")
    @NotBlank(message = "Tipo de usuario invalido")
    String role;
}
