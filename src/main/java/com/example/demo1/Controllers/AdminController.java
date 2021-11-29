package com.example.demo1.Controllers;

import com.example.demo1.DTOs.DoctorDTO;
import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.News;
import com.example.demo1.Entities.Specialization;
import com.example.demo1.Entities.User;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.Repositories.NewsRepository;
import com.example.demo1.Repositories.SampleRepository;
import com.example.demo1.Repositories.SpecializationRepository;
import com.example.demo1.UserNotFoundException;
import com.sun.mail.iap.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RestController
@AllArgsConstructor
public class AdminController {

    @Autowired
    SampleRepository sampleRepository;
    DoctorRepository doctorRepository;
    SpecializationRepository specializationRepository;
    NewsRepository newsRepository;

    @PostMapping("/addNews")
    public ResponseEntity<News> entity(@RequestBody News news) {
        newsRepository.save(news);
        return ResponseEntity.ok(news);
    }

    @GetMapping("/findAllDoctors")
    public ResponseEntity<List<Doctor>> findAll() {
        return ResponseEntity.ok(doctorRepository.findAll());
    }


    @PostMapping("/signUpDoctor")
    public ResponseEntity<Doctor> signUp(@RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        doctor.setUser(sampleRepository.findById(doctorDTO.getUser_id()).orElse(null));
        doctor.setLast_name(doctorDTO.getLast_name());
        doctor.setName(doctorDTO.getName());
        doctor.setDoctor_specialization(specializationRepository.findById(doctorDTO.getSpecialization_id()).orElse(null));
        doctorRepository.save(doctor);

        return ResponseEntity.ok(doctor);
    }



}
