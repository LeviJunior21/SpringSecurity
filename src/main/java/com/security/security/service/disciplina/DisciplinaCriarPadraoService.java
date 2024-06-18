package com.security.security.service.disciplina;

import com.security.security.dto.disciplina.DisciplinaDTO;
import com.security.security.model.Disciplina;
import com.security.security.repositories.DisciplinaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplinaCriarPadraoService implements DisciplinaCriarService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    DisciplinaRepository disciplinaRepository;

    @Override
    public DisciplinaDTO criar(DisciplinaDTO disciplinaDTO) {
        Disciplina disicplina = modelMapper.map(disciplinaDTO, Disciplina.class);
        Disciplina disciplinaSaved = disciplinaRepository.save(disicplina);
        return modelMapper.map(disciplinaSaved, DisciplinaDTO.class);
    }
}
