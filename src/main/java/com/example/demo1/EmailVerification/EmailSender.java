package com.example.demo1.EmailVerification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@Service
public class EmailSender {

    public static final String[] subjects = new String []{
            "email verification", "account cancellacion", "other stuff"
    };

    @Autowired
    JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String receiverEmail, String token) {
        StringBuilder builder = new StringBuilder();
        builder.append("To jest Twój link aktywacyjny\n : Kliknij w niego by aktywować konto \n");
        builder.append("<a> localhost:8080/signUp?token=");
        builder.append(subjects[0]);
        builder.append("</a>");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("clinic@noreply.com");
        message.setTo(receiverEmail);
        message.setSubject(subjects[0]);
        message.setText(token);
        javaMailSender.send(message);

    }
}

