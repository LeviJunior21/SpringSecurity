package com.security.security.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/v1/disciplinas",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class DisciplinaController {

    @GetMapping()
    public String getAll() {
        return "Voce esta autorizado a ver todas as disciplinas";
    }
}
