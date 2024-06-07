package com.security.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.security.dto.usuario.UsuarioDTO;
import com.security.security.dto.usuario.UsuarioPostRequestDTO;
import com.security.security.model.Usuario;
import com.security.security.repositories.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Casos de teste do controlador do usuario")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioV1ControllerTest {

    @Autowired
    MockMvc driver;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ObjectMapper objectMapper;

    String URI_USUARIO = "/v1/usuarios";
    Usuario usuario;
    UsuarioPostRequestDTO usuarioPostRequestDTO;
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @BeforeEach
    void setup() {
        usuario = Usuario.builder()
                .nome("Levi")
                .senha("123456")
                .build();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        usuario = usuarioRepository.save(usuario);

        usuarioPostRequestDTO = UsuarioPostRequestDTO.builder()
                .nome("LeviJunior")
                .senha("123456")
                .build();
    }


    @Nested
    @DisplayName("Cados de testes que passam.")
    class CasosQuePassam {

        @Test
        @DisplayName("Quando criamos um usuário com dados validos.")
        void quandoCriamosUmUsuarioComDadosValidos() throws Exception {
            String response = driver.perform(post(URI_USUARIO + "/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioPostRequestDTO))
                    )
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            UsuarioDTO usuarioDTO = objectMapper.readValue(response, UsuarioDTO.class);

            assertEquals(usuarioPostRequestDTO.getNome(), usuarioDTO.getNome());
        }
    }
}
