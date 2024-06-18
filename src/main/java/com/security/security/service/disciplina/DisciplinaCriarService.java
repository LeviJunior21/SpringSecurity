package com.security.security.service.disciplina;

import com.security.security.dto.disciplina.DisciplinaDTO;

@FunctionalInterface
public interface DisciplinaCriarService {
    DisciplinaDTO criar(DisciplinaDTO disciplinaDTO);
}
