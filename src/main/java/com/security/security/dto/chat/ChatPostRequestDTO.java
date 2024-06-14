package com.security.security.dto.chat;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatPostRequestDTO {
    @JsonProperty("remetente")
    @NotNull(message = "Id do remetente eh invalido.")
    private Long remetente;

    @JsonProperty("receptor")
    @NotNull(message = "Id do receptor eh invalido.")
    private Long receptor;

    @JsonProperty("mensagem")
    @NotEmpty(message = "Conteudo nao pode estar vazio.")
    private String mensagem;

    @JsonProperty("timestamp")
    @NotNull(message = "Data invalida.")
    private Date timestamp;

    public ChatPostRequestDTO(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatPostRequestDTO chatPostPutRequestDTO = objectMapper.readValue(jsonString, ChatPostRequestDTO.class);
        this.remetente = chatPostPutRequestDTO.getRemetente();
        this.receptor = chatPostPutRequestDTO.getReceptor();
        this.mensagem = chatPostPutRequestDTO.getMensagem();
        this.timestamp = chatPostPutRequestDTO.getTimestamp();
    }
}
