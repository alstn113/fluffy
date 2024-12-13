package com.fluffy.external.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
public abstract class AbstractEmailSender<T extends SendEmailRequest> {

    protected final JavaMailSender javaMailSender;

    protected static final String FROM_EMAIL = "fluffy <noreply@fluffy.run>";

    public void send(T email) {
        try {
            MimeMessage mimeMessage = createMimeMessage(email);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | MailException e) {
            throw new IllegalArgumentException("메일을 보내는 중 오류가 발생했습니다.", e);
        }
    }

    protected abstract MimeMessage createMimeMessage(T email) throws MessagingException;
}