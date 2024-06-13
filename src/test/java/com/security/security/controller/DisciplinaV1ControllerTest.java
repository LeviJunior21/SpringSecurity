package com.security.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.security.dto.login.AuthenticationRequestDTO;
import com.security.security.model.Usuario;
import com.security.security.repositories.UsuarioRepository;
import com.security.security.service.jwt.JwtService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Casos de teste do controlador de disciplinas.")
public class DisciplinaV1ControllerTest {
    @Autowired
    MockMvc driver;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtService jwtService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    String URI_DISCIPLINAS = "/v1/disciplinas";
    String authorizationHeader;
    AuthenticationRequestDTO authenticationRequestDTO;
    UsuarioAuthenticated usuarioAuthenticated;
    Usuario usuario;

    @BeforeEach
    void setup() {
        usuario= Usuario.builder()
                .nome("Levi")
                .senha("123456")
                .build();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario = usuarioRepository.save(usuario);

        authenticationRequestDTO = authenticationRequestDTO.builder()
                .nome(usuario.getNome())
                .senha("123456")
                .build();

        usuarioAuthenticated = new UsuarioAuthenticated();
        authorizationHeader = "Bearer " + getToken(authenticationRequestDTO);
    }

    @AfterEach
    void tearDown() {
        usuarioRepository.deleteAll();
    }

    private String getToken(AuthenticationRequestDTO authenticationRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getNome(), authenticationRequestDTO.getSenha(), usuarioAuthenticated.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UsuarioAuthenticated usuarioAuthenticated = new UsuarioAuthenticated();
        usuarioAuthenticated.setNome(userDetails.getUsername());
        usuarioAuthenticated.setSenha(userDetails.getPassword());

        String token = jwtService.gerarToken(usuarioAuthenticated);
        return token;
    }

    @Nested
    @DisplayName("Casos de testes que passam.")
    class CasosDeTestesQuePassam {

        @Test
        @DisplayName("Quando buscamos pelas disciplinas com token valido.")
        void quandoBuscamosPelasDisciplinasComTokenValido() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setUp

            // Act
            String response = driver.perform(get(URI_DISCIPLINAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            String resp = response;
            assertEquals("Voce esta autorizado a ver todas as disciplinas", resp);
        }
    }
}
