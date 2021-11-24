package com.example.demo1.Controllers;

import com.example.demo1.Entities.Patient;
import com.example.demo1.Prototypes.ResponseMessages;
import com.example.demo1.Repositories.PatientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@CrossOrigin("http://localhost:4200")
public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping("/welcome")
    ResponseEntity<ResponseMessages> checkAuthorities() {
        return ResponseEntity.ok(new ResponseMessages("Siema"));
    }


    @PostMapping
    ResponseEntity<Patient> getFormVisitAppointment(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientRepository.save(patient));
    }

}
