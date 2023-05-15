package com.example.demo1.Helpers;

import com.example.demo1.Services.EmailSender;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Repositories.VisitRepository;
import com.example.demo1.Services.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;

@EnableScheduling
@EnableAsync
@Service
@Transactional
@RequiredArgsConstructor
public class BackgroundTasks {

    private final EmailSender emailSender;
    private final VisitRepository visitRepository;
    private final SmsSender smsSender;

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

        tommorowVisits.forEach(medicalVisit -> {
            try {
                createReminderMessageTemplate(medicalVisit);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

    }

    private void createReminderMessageTemplate(MedicalVisit f) throws MessagingException {
        emailSender.sendMail(f.getPatientId().getUser().getEmail(), "Powiadomienie o wizycie",
                "Chcialbym poinformowac o"  +
                "jutrzejszej wizycie, ktora odbedzie sie " + f.getStartDate().toString() + " u "
                        + f.getDoctorId().getName() + " " + f.getDoctorId().getLastName() + "<br>" +
                "Wiadomosc zostala wygenerowana automatycznie, prosimy nie odpowiadac");
        smsSender.sendSms(f.getPatientId().getPhone(), f);
    }

}
