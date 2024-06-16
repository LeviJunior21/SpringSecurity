package com.security.security.configuration;

import com.security.security.exception.usuario.UsuarioNaoExisteException;
import com.security.security.model.Usuario;
import com.security.security.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.net.http.HttpHeaders;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat");
        registry.addEndpoint("/chat").setAllowedOriginPatterns("http://localhost:3000").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/public", "/user");
        registry.setUserDestinationPrefix("/user");
    }

    @Bean
    public WebSocketStompClient stompClient() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return stompClient;
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                        super.afterConnectionEstablished(session);
                        LOGGER.info("Conectado");
                        Long id = getId(session);

                        if (id != null) {
                            Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNaoExisteException::new);
                            usuario.setOnline(true);
                            usuarioRepository.save(usuario);
                        }

                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        super.afterConnectionClosed(session, closeStatus);
                        LOGGER.info("Desconectado");
                        Long id = getId(session);

                        if (id != null) {
                            Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNaoExisteException::new);
                            usuario.setOnline(false);
                            usuarioRepository.save(usuario);
                        }
                    }

                    @Override
                    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                        super.handleMessage(session, message);
                        String messagePayload = message.getPayload().toString();
                        String headers = session.getHandshakeHeaders().toString();
                        LOGGER.info("headers " + headers + message.getPayload());
                        LOGGER.info("Mensagem recebida: " + messagePayload);
                    }

                    private Long getId(WebSocketSession session) {
                        Long id = null;
                        String query = session.getUri().getQuery();
                        if (query != null && query.startsWith("userId=")) {
                            String userId = query.split("=")[1];
                            id = Long.parseLong(userId);
                        }
                        return id;
                    }
                };
            }
        });
    }
}
