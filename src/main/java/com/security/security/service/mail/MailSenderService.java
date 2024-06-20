package com.security.security.service.mail;

@FunctionalInterface
public interface MailSenderService {
    String sendMail(String destinatario, String assunto, String mensagem);
}
