package com.security.security.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Informações necessárias para ter o Token.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO {
    @Schema(description = "Nome do usuário", defaultValue = "Seu nome", example = "João")
    @JsonProperty("nome")
    @NotBlank(message = "Nome invalido")
    String nome;

    @Schema(description = "Senha do usuário", defaultValue = "Sua senha", example = "99999999")
    @JsonProperty("senha")
    @NotBlank(message = "Senha invalida")
    String senha;
}
