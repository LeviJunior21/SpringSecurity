package com.security.security.controller;

import com.security.security.dto.disciplina.DisciplinaDTO;
import com.security.security.service.disciplina.DisciplinaCriarService;
import com.security.security.service.disciplina.DisciplinaListarService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/disciplinas",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class DisciplinaController {
    @Autowired
    DisciplinaCriarService disciplinaCriarService;
    @Autowired
    DisciplinaListarService disciplinaListarService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody DisciplinaDTO disciplinaDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(disciplinaCriarService.criar(disciplinaDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestHeader(HttpHeaders.AUTHORIZATION) @Parameter(hidden = true) String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(disciplinaListarService.ListarDisciplinas(pageable));
    }
}
