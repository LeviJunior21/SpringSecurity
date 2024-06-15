package com.security.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class EnableHandlerConfig implements WebSocketConfigurer {
    @Autowired
    private ChatHandler chatHandler;

    // Ver EnableWebSocket ainda para o mesmo endpoint /chat.
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/ch").setAllowedOrigins("http://localhost:3000").setAllowedOriginPatterns("http://localhost:3000");
        registry.addHandler(chatHandler, "/ch").setAllowedOrigins("http://localhost:3000").setAllowedOriginPatterns("http://localhost:3000").withSockJS();
    }
}
