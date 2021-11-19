package com.example.demo1.Services;

import com.example.demo1.Entities.ContactForm;
import com.example.demo1.Repositories.ContactFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactFormService {

    @Autowired
    private ContactFormRepository contactFormRepository;

    public ResponseEntity<List<ContactForm>> getAllForms() {
        return ResponseEntity.ok(contactFormRepository.findAll());
    }

    public ResponseEntity<List<ContactForm>> getTodayForms() {
        List<ContactForm> todaySentForms = contactFormRepository.findAll().stream().filter(e -> e.getDate().equals(new Date()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(todaySentForms);

    }

    public ResponseEntity<ContactForm> addNewContactForm(ContactForm contactForm) {
        return ResponseEntity.ok(contactFormRepository.save(contactForm));
    }

}
