package com.security.security.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Autenticação do usuário")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    @Schema(description = "Token do usuário", defaultValue = "token", example = "token")
    @JsonProperty("token")
    @NotBlank(message = "Token invalido")
    String token;
}
