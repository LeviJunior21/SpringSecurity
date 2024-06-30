package com.security.security.controller;

import com.security.security.dto.disciplina.DisciplinaDTO;
import com.security.security.service.disciplina.DisciplinaCriarService;
import com.security.security.service.disciplina.DisciplinaListarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class DisciplinaV1Controller {
    @Autowired
    DisciplinaCriarService disciplinaCriarService;
    @Autowired
    DisciplinaListarService disciplinaListarService;

    @Operation(description = "Cria uma disciplina (precisa de autorização com Token e papel de ADMIN).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a disciplina criada"),
            @ApiResponse(responseCode = "400", description = "A disciplina já existe."),
            @ApiResponse(responseCode = "401", description = "O usuário não está autorizado.")
    })
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody DisciplinaDTO disciplinaDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(disciplinaCriarService.criar(disciplinaDTO));
    }

    @Operation(description = "Retorna uma páginação de disciplinas criadas (precisa de autorização com Token).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a paginação das disciplinas criadas."),
            @ApiResponse(responseCode = "401", description = "Usuário não está autorizado.")
    })
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
