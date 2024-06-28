package com.security.security.dto.disciplina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Nome da disciplina.")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisciplinaDTO {
    @Schema(description = "Nome da disciplina", defaultValue = "Nome da disciplina", example = "Teoria da Computação")
    @NotBlank(message = "Nome da disciplina esta invalido")
    String nome;
}
