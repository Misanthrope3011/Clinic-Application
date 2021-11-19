package com.example.demo1.Controllers;

import com.example.demo1.Entities.Patient;
import com.example.demo1.Repositories.PatientRepository;
import com.sun.mail.iap.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private PatientRepository patientRepository;

    @PostMapping
    ResponseEntity<Patient> getFormVisitAppointment(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientRepository.save(patient));
    }

}
