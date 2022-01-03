package com.example.demo1.Helpers;

import com.example.demo1.EmailVerification.EmailSender;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Entities.Patient;
import com.example.demo1.Repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.HashSet;

@EnableScheduling
@EnableAsync
@Service
@Transactional
public class BackgroundTasks {

    @Autowired
    EmailSender emailSender;
    @Autowired
    VisitRepository visitRepository;

    @Async
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendRemainders() throws InterruptedException {

        HashSet<MedicalVisit> tommorowVisits = new HashSet<>();

        visitRepository.findAll()
        .forEach(e -> {
            if ((e.getStartDate().toLocalDate().getDayOfYear()) - 1 == LocalDateTime.now().getDayOfYear()) {
                tommorowVisits.add(e);
            }
        });

        tommorowVisits.forEach(f -> {
            try {
                emailSender.sendMail(f.getPatient_id().getUser().getEmail(), "Powiadomienie o wizycie", "Chcialbym poinformowac o"  +
                        "jutrzejszej wizycie, ktora odbedzie sie " + f.getStartDate().toString() + "<br>" +
                        "Wiadomosc zostala wygenerowana automatycznie, prosimy nie odpowiadac");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

    }

}
