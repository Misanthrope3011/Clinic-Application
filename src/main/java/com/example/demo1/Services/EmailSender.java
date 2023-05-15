package com.example.demo1.Services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class EmailSender {

    private final JavaMailSender mailSender;
    private final String CONFIRM_MESSAGE_SUBJECT = "Rejestracja konta w przychodni";

    @Async
    public void sendMail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = this.getMailSender().createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("przychodniahealthcare@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }

}

