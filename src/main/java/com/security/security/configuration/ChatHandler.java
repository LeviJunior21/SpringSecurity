package com.security.security.configuration;

import com.security.security.exception.usuario.UsuarioNaoExisteException;
import com.security.security.model.Usuario;
import com.security.security.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ChatHandler extends TextWebSocketHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(ChatHandler.class);
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<WebSocketSession>();
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Conectado ao servidor");

        Long idUsuario = getUserIdFromSession(session);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(UsuarioNaoExisteException::new);
        sessions.add(session);
        usuario.setOnline(true);
        usuarioRepository.save(usuario);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("Desconectado do servidor");

        Long idUsuario = getUserIdFromSession(session);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(UsuarioNaoExisteException::new);
        sessions.remove(session);
        usuario.setOnline(false);
        usuarioRepository.save(usuario);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String newMessage = message.getPayload();
        LOGGER.info("Mensagem enviada ao servidor: " + newMessage);

        synchronized (sessions) {
            for (WebSocketSession webSocketSession : sessions) {
                webSocketSession.sendMessage(message);
            }
        }
    }

    private Long getUserIdFromSession(WebSocketSession session) {
        Long idUsuario = usuarioRepository.findAll().stream().findFirst().get().getId();
        return idUsuario;
    }
}
