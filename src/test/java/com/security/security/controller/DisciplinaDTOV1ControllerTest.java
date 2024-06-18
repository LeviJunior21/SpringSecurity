package com.security.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.security.dto.disciplina.DisciplinaDTO;
import com.security.security.dto.login.AuthenticationRequestDTO;
import com.security.security.model.Disciplina;
import com.security.security.model.Usuario;
import com.security.security.repositories.DisciplinaRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Casos de teste do controlador de disciplinas.")
public class DisciplinaDTOV1ControllerTest {
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
    @Autowired
    DisciplinaRepository disciplinaRepository;

    String URI_DISCIPLINAS = "/v1/disciplinas";
    String authorizationHeader;
    AuthenticationRequestDTO authenticationRequestDTO;
    Usuario usuarioAuthenticated;
    Usuario usuario;
    Disciplina disciplina;
    DisciplinaDTO disciplinaDTO;

    @BeforeEach
    void setup() {
        usuario = Usuario.builder()
                .nome("Levi")
                .senha("123456")
                .role("ROLE_ADMIN")
                .build();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario = usuarioRepository.save(usuario);

        authenticationRequestDTO = authenticationRequestDTO.builder()
                .nome(usuario.getNome())
                .senha("123456")
                .build();

        usuarioAuthenticated = Usuario.builder()
                .nome(usuario.getNome())
                .senha("123456")
                .role(usuario.getRole())
                .build();
        authorizationHeader = "Bearer " + getToken(authenticationRequestDTO);

        disciplina = Disciplina.builder()
                .nome("Teoria da Computação")
                .build();

        disciplina = disciplinaRepository.save(disciplina);

        disciplinaDTO = DisciplinaDTO.builder()
                .nome("Teoria da Computação")
                .build();
    }

    @AfterEach
    void tearDown() {
        usuarioRepository.deleteAll();
        disciplinaRepository.deleteAll();
    }

    private String getToken(AuthenticationRequestDTO authenticationRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getNome(), authenticationRequestDTO.getSenha(), usuarioAuthenticated.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuarioAuthenticated = new Usuario();
        usuarioAuthenticated.setNome(userDetails.getUsername());
        usuarioAuthenticated.setSenha(userDetails.getPassword());
        usuarioAuthenticated.setRole("ROLE_ADMIN");

        String token = jwtService.gerarToken(usuarioAuthenticated);
        return token;
    }

    @Nested
    @DisplayName("Casos de testes que passam.")
    class CasosDeTestesQuePassam {

        @Test
        @DisplayName("Quando criamos uma disciplina com token valido")
        void quandoCriamosUmaDisciplinaComTokenValido () throws Exception {
            // Arrange
            // Nenhuma necessidade alem do setUp

            // Act
            String response = driver.perform(post(URI_DISCIPLINAS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(disciplinaDTO))
                            .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            DisciplinaDTO disciplinaDTOResponse = objectMapper.readValue(response, DisciplinaDTO.class);
            assertEquals(disciplinaDTO.getNome(), disciplinaDTOResponse.getNome());
        }

        @Test
        @DisplayName("Quando buscamos pelas disciplinas com token valido.")
        void quandoBuscamosPelasDisciplinasComTokenValido() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setUp

            // Act
            String response = driver.perform(get(URI_DISCIPLINAS + "?page=0&size=10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            assertTrue(response.contains("Teoria da Computação"));
        }
    }
}
