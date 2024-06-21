package com.security.security.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderPadraoService implements MailSenderService {
    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    String remetente;
    Logger LOGGER = LoggerFactory.getLogger(MailSenderPadraoService.class);
    public void sendMail(String destinatario, String assunto, String mensagem) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(mensagem);
            javaMailSender.send(message);
            LOGGER.info("Email enviado com sucesso!");
        } catch (Exception e) {
            LOGGER.info("Erro ao enviar e-mail");
        }
    }
}
