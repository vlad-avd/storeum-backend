package com.avdienko.storeum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.avdienko.storeum.util.Constants.BASE_URL;
import static com.avdienko.storeum.util.Constants.HOST;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Async
    public void send(String sendTo, String firstName, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(sendTo);
            helper.setSubject("Confirm your email");
            helper.setText(buildEmailBody(firstName, token), true);
            mailSender.send(mimeMessage);
            log.info("Confirmation email send successfully");
        } catch (MessagingException ex) {
            log.info("Failed to send email to={}, ex={}", sendTo, ex);
        }
    }

    private String buildEmailBody(String firstName, String token) {
        String url = String.format("%s%s/auth/confirm?token=%s", HOST, BASE_URL, token);
        return """
                <html>
                    Hello, <b>%s</b>. Please click on link below to activate your account:
                    <br>
                    <a href="%s">Activate Now</a>
                </html>
                """
                .formatted(firstName, url);
    }
}
