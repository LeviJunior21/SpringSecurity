package com.security.security.dto.chat;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Schema(description = "Mensagem do usuário de origem para o usuário de destino")
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatPostRequestDTO {
    @Schema(description = "ID do remetente", defaultValue = "ID do remetente", example = "9")
    @JsonProperty("remetente")
    @NotNull(message = "Id do remetente eh invalido.")
    private Long remetente;

    @Schema(description = "ID do destinatário", defaultValue = "ID do destinatário", example = "10")
    @JsonProperty("receptor")
    @NotNull(message = "Id do receptor eh invalido.")
    private Long receptor;

    @Schema(description = "Mensagem enviada do usuário remetente", defaultValue = "Mensagem do usuário", example = "Oi!")
    @JsonProperty("mensagem")
    @NotEmpty(message = "Conteudo nao pode estar vazio.")
    private String mensagem;

    @Schema(description = "Nome do usuário", defaultValue = "Seu nome", example = "Seu nome")

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
