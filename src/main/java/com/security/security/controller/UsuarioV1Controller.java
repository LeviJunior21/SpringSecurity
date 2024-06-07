package com.security.security.controller;

import com.security.security.dto.login.AuthenticationRequestDTO;
import com.security.security.dto.usuario.UsuarioPostRequestDTO;
import com.security.security.service.autenticacao.AuthenticationService;
import com.security.security.service.usuario.UsuarioCreateService;
import com.security.security.service.usuario.UsuarioGetService;
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

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.autenticar(authenticationRequestDTO));
    }

    @PostMapping("/create")
    public ResponseEntity<?> criar(@RequestBody UsuarioPostRequestDTO usuarioPostRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioCreateService.criar(usuarioPostRequestDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioGetService.get(null));
    }
}
