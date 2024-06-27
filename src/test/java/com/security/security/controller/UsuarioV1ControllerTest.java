package com.security.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.security.dto.login.AuthenticationRequestDTO;
import com.security.security.dto.login.LoginResponseDTO;
import com.security.security.dto.usuario.UsuarioDTO;
import com.security.security.dto.usuario.UsuarioPostRequestDTO;
import com.security.security.exception.CustomErrorType;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Casos de teste do controlador do usuario")
public class UsuarioV1ControllerTest {

    @Autowired
    MockMvc driver;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    String URI_USUARIO = "/v1/usuarios";
    Usuario usuario;
    UsuarioPostRequestDTO usuarioPostRequestDTO;
    AuthenticationRequestDTO authenticationRequestDTO;
    String authorizationHeader;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .nome("Levi")
                .senha("123456")
                .role("ROLE_ADMIN")
                .email("leviiiiiiiiiiiiiiiiiiii@gmail.com")
                .build();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        usuario = usuarioRepository.save(usuario);

        usuarioPostRequestDTO = UsuarioPostRequestDTO.builder()
                .nome("LeviJunior")
                .senha("123456")
                .role("ROLE_ADMIN")
                .email("leviiiiiiiiiiiiiiiiiiii@gmail.com")
                .build();

        authenticationRequestDTO = AuthenticationRequestDTO.builder()
                .nome(usuario.getNome())
                .senha("123456")
                .build();

        authorizationHeader = "Bearer " + getToken(authenticationRequestDTO);
    }

    private String getToken(AuthenticationRequestDTO authenticationRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getNome(), authenticationRequestDTO.getSenha(), usuario.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuarioAuthenticated = new Usuario();
        usuarioAuthenticated.setNome(userDetails.getUsername());
        usuarioAuthenticated.setSenha(userDetails.getPassword());
        usuarioAuthenticated.setRole("ROLE_ADMIN");

        String token = jwtService.gerarToken(usuarioAuthenticated);
        return token;
    }

    @AfterEach
    void tearDown() {
        usuarioRepository.deleteAll();
    }

    @Nested
    @DisplayName("Cados de testes que passam.")
    class CasosQuePassam {

        @Test
        @DisplayName("Quando criamos um usuário com dados validos.")
        void quandoCriamosUmUsuarioComDadosValidos() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setUp

            // Act
            String response = driver.perform(post(URI_USUARIO + "/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioPostRequestDTO))
                    )
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            UsuarioDTO usuarioDTO = objectMapper.readValue(response, UsuarioDTO.class);
            assertEquals(usuarioPostRequestDTO.getNome(), usuarioDTO.getNome());
        }

        @Test
        @DisplayName("Quando fazemos login com um usuário com dados validos.")
        void quandoFazemosLoginComUmUsuarioComDadosValidos() throws Exception {
            // Arrange
            // Nenhuma necessidade além do setUp

            // Act
            String response = driver.perform(post(URI_USUARIO + "/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authenticationRequestDTO))
                    )
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            LoginResponseDTO loginResponseDTO = objectMapper.readValue(response, LoginResponseDTO.class);
            assertNotNull(loginResponseDTO.getToken());
        }
    }

    @Nested
    @DisplayName("Casos de testes que falham.")
    class CasosDeTestesQueFalham {

        @Test
        @DisplayName("Quando criamos um usuário com nome invalido.")
        void quandoCriamosUmUsuarioComNomeInvalido() throws Exception {
            // Arrange
            usuarioPostRequestDTO.setNome(null);

            // Act
            String response = driver.perform(post(URI_USUARIO + "/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioPostRequestDTO))
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            CustomErrorType customErrorType = objectMapper.readValue(response, CustomErrorType.class);
            assertEquals("Erros de validacao encontrados", customErrorType.getMessage());
            assertEquals("Nome invalido", customErrorType.getErrors().get(0));
        }

        @Test
        @DisplayName("Quando criamos um usuário com senha invalida.")
        void quandoCriamosUmUsuarioComSenhaValida() throws Exception {
            // Arrange
            usuarioPostRequestDTO.setSenha(null);

            // Act
            String response = driver.perform(post(URI_USUARIO + "/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(usuarioPostRequestDTO))
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            CustomErrorType customErrorType = objectMapper.readValue(response, CustomErrorType.class);
            assertEquals("Erros de validacao encontrados", customErrorType.getMessage());
            assertEquals("Senha invalida", customErrorType.getErrors().get(0));
        }

        @Test
        @DisplayName("Quando deletamos todos os usuários com Role Invalido")
        void quandoDeletamosTodosOsUsuariosComRoleInvalido() throws Exception {
            // Arrange
            usuario.setRole("ROLE_PROGRAMMER");
            usuarioRepository.save(usuario);

            // Act
            String response = driver.perform(post(URI_USUARIO + "/deletarAll")
                            .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            CustomErrorType customErrorType = objectMapper.readValue(response, CustomErrorType.class);
            assertEquals("Erros de validacao encontrados", customErrorType.getMessage());
            assertEquals("O usuario nao é um administrador", customErrorType.getErrors().get(0));
        }

        @Test
        @DisplayName("Quando deletamos todos os usuários com usuário não habilitado")
        void quandoDeletamosTodosOsUsuariosComUsuarioNaoHabilitado() throws Exception {
            // Arrange
            usuario.setHabilitado(false);
            usuarioRepository.save(usuario);

            // Act
            String response = driver.perform(post(URI_USUARIO + "/deletarAll")
                            .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            // Assert
            CustomErrorType customErrorType = objectMapper.readValue(response, CustomErrorType.class);
            assertEquals("Erros de validacao encontrados", customErrorType.getMessage());
            assertEquals("O usuário está desabilitado", customErrorType.getErrors().get(0));
        }
    }
}
