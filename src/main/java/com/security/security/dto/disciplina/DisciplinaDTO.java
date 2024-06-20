package com.security.security.dto.disciplina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Token do usuário para acessar o sistema.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaDTO {
    @Schema(description = "Token do usuário", defaultValue = "Bearer SeuToken", example = "Bearer SeuToken")
    @NotBlank(message = "Nome da disciplina esta invalido")
    String nome;
}
