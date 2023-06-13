package com.ll.tenmindaily.boundedContext.emailSender.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


public interface EmailSenderService {
    void send(String to, String from, String title, String body);
}


@Service
@RequiredArgsConstructor
class GmailEmailSenderService implements EmailSenderService {
    private final JavaMailSender mailSender;

    @Override
    public void send(String to, String from, String title, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(title);
            message.setContent(body, "text/html;charset=euc-kr");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }
}
