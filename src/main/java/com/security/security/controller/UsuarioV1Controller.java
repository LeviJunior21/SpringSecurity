package com.security.security.controller;

import com.security.security.dto.login.AuthenticationRequestDTO;
import com.security.security.dto.usuario.UsuarioPostRequestDTO;
import com.security.security.service.autenticacao.AuthenticationService;
import com.security.security.service.usuario.UsuarioCreateService;
import com.security.security.service.usuario.UsuarioDeleteService;
import com.security.security.service.usuario.UsuarioGetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/usuarios",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class UsuarioV1Controller {
    @Autowired
    UsuarioCreateService usuarioCreateService;
    @Autowired
    UsuarioGetService usuarioGetService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    UsuarioDeleteService usuarioDeleteService;

    @Operation(description = "Busca o token para poder acessar serviços que necessitam de autorização")
    @ApiResponse(description = "Retorna o Token exclusivo do usuário válido por até após sua a criação 1 hora.")
    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody @Valid AuthenticationRequestDTO authenticationRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.autenticar(authenticationRequestDTO));
    }

    @Operation(description = "Cria um usuário para poder acessar serviços da aplicação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário criado."),
            @ApiResponse(responseCode = "400", description = "O usuário já existe.")
    })
    @PostMapping("/create")
    public ResponseEntity<?> criar(@RequestBody @Valid UsuarioPostRequestDTO usuarioPostRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioCreateService.criar(usuarioPostRequestDTO));
    }

    @Operation(description = "Busca por todos os usuários criados (não necessita de autorização do token)")
    @ApiResponse(description = "Retorna uma lista de usuários")
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioGetService.get(null));
    }

    @Operation(description = "Deleta um usuário que já foi criado (necessita de autorização e ter permissão de ADMIN).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "203", description = "O usuário foi deleta."),
            @ApiResponse(responseCode = "400", description = "O usuário não existe.")
    })
    @DeleteMapping("/deletar")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        usuarioDeleteService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Usuario deletado com sucesso");
    }

    @Operation(description = "Deleta todos os usuários que já foram criados (necessita de autorização e ter permissão de ADMIN).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário criado."),
            @ApiResponse(responseCode = "400", description = "O usuário já existe.")
    })
    @DeleteMapping("/deletarAll")
    public ResponseEntity<?> deleteAll() {
        usuarioDeleteService.delete(null);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Usuarios deletados com sucesso");
    }
}
