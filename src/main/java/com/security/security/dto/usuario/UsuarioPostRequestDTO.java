package com.security.security.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
    @Schema(description = "Nome do usuário a ser criado.", defaultValue = "Seu nome", example = "João")
    @JsonProperty("nome")
    @NotBlank(message = "Nome invalido")
    String nome;

    @Schema(description = "Email do usuário a ser criado.", defaultValue = "seuemail@gmail.com", example = "joao@gmail.com")
    @Email
    @JsonProperty("email")
    @NotBlank(message = "Email invalido")
    String email;

    @Schema(description = "Senha do usuário a ser criado.", defaultValue = "Sua senha", example = "99999999")
    @JsonProperty("senha")
    @NotBlank(message = "Senha invalida")
    String senha;

    @Schema(description = "Papel do usuário a ser criado.", defaultValue = "ROLE_USUARIO", example = "ROLE_ADMIN")
    @JsonProperty("role")
    @NotBlank(message = "Tipo de papel invalido")
    String role;
}
