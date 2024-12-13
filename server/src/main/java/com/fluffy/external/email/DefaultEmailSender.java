package com.fluffy.external.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class DefaultEmailSender extends AbstractEmailSender<SendEmailRequest> {

    public DefaultEmailSender(JavaMailSender javaMailSender) {
        super(javaMailSender);
    }

    @Override
    public MimeMessage createMimeMessage(SendEmailRequest email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        helper.setFrom(FROM_EMAIL);
        helper.setTo(email.to());
        helper.setSubject(email.subject());
        helper.setText(email.body(), true);

        return mimeMessage;
    }
}
