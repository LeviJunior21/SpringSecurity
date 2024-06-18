package com.security.security.service.disciplina;

import com.security.security.dto.disciplina.DisciplinaDTO;
import com.security.security.model.Disciplina;
import com.security.security.repositories.DisciplinaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaListarPadraoService implements DisciplinaListarService {
    @Autowired
    DisciplinaRepository disciplinaRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<DisciplinaDTO> ListarDisciplinas(Pageable pageable) {
        Page<Disciplina> disciplinas = disciplinaRepository.findAll(pageable);
        Page<DisciplinaDTO> disciplinaDTOS = disciplinas.map(disciplina -> modelMapper.map(disciplina, DisciplinaDTO.class));
        return disciplinaDTOS;
    }
}
