package com.example.demo1.controller;

import com.example.demo1.entity.ContactForm;
import com.example.demo1.entity.Doctor;
import com.example.demo1.entity.News;
import com.example.demo1.service.ContactFormService;
import com.example.demo1.service.DoctorUtilsService;
import com.example.demo1.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final DoctorUtilsService doctorUtilsService;
    private final NewsService newsService;
    private final ContactFormService contactFormService;

    @PostMapping("/addNews")
    public ResponseEntity<News> entity(@RequestBody News news) {
        return ResponseEntity.ok(newsService.createNews(news));
    }

    @GetMapping("/getContactForms")
    public ResponseEntity<List<ContactForm>> contactForm() {
        return contactFormService.getAllForms();
    }


    @GetMapping("/findAllDoctors")
    public ResponseEntity<List<Doctor>> findAll() {
        return ResponseEntity.ok(doctorUtilsService.findAllDoctors());
    }

}
