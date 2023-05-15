package com.example.demo1.Services;

import com.example.demo1.Entities.ContactForm;
import com.example.demo1.Repositories.ContactFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactFormService {

    private final ContactFormRepository contactFormRepository;

    public ResponseEntity<List<ContactForm>> getAllForms() {
        return ResponseEntity.ok(contactFormRepository.findAll());
    }

    public ResponseEntity<List<ContactForm>> getTodayForms() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<ContactForm> todaySentForms = contactFormRepository.findAll().stream().filter(e ->
                e.getDate().toString().equals(LocalDateTime.now().format(formatter)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(todaySentForms);

    }

    public ResponseEntity<ContactForm> addNewContactForm(ContactForm contactForm) {
        return ResponseEntity.ok(contactFormRepository.save(contactForm));
    }

}
