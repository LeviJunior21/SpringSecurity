package com.security.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    Long id;

    @JsonProperty("nome")
    String nome;

    @JsonProperty("senha")
    String senha;

    @JsonProperty("habilitado")
    @Builder.Default
    Boolean habilitado = true;
}
