package com.security.security.service.mail;

@FunctionalInterface
public interface MailSenderService {
    void sendMail(String destinatario, String assunto, String mensagem);
}
