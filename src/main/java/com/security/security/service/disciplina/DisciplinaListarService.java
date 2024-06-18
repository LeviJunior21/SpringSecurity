package com.security.security.service.disciplina;

import com.security.security.dto.disciplina.DisciplinaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@FunctionalInterface
public interface DisciplinaListarService {
    Page<DisciplinaDTO> ListarDisciplinas(Pageable pageable);
}
