package com.security.security.controller;

import com.security.security.dto.chat.ChatPostRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class ChatV1Controller {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(ChatV1Controller.class);

    @MessageMapping("/private-message")
    public ChatPostRequestDTO chat(@Payload ChatPostRequestDTO chatPostPutRequestDTO) {
        LOGGER.info("Mensagem recebida no controller: " + chatPostPutRequestDTO.getMensagem());
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatPostPutRequestDTO.getReceptor()), "/private", chatPostPutRequestDTO);
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(chatPostPutRequestDTO.getRemetente()), "/private", chatPostPutRequestDTO);

        return chatPostPutRequestDTO;
    }
}
