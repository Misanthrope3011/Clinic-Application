package com.example.demo1.Controllers;

import com.example.demo1.DTOs.Prototype;
import com.example.demo1.Entities.Patient;
import com.example.demo1.Entities.User;
import com.example.demo1.Prototypes.ResponseMessages;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Repositories.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@CrossOrigin("http://localhost:4200")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SampleRepository sampleRepository;

    @GetMapping("/welcome")
    ResponseEntity<ResponseMessages> checkAuthorities() {
        return ResponseEntity.ok(new ResponseMessages("Siema"));
    }

    @PostMapping("/getProfile")
    ResponseEntity getPatient(@RequestBody Prototype id) {
        User user = sampleRepository.findAll().get(id.getId().intValue());
        if(user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.badRequest().body("Nie znaleziono");
    }


    @PutMapping("/editProfile")
    ResponseEntity editInfo(@RequestBody User user) {
        User edited = sampleRepository.findById(user.getId()).orElse(null);

       return ResponseEntity.ok("XD");
    }
    @PostMapping
    ResponseEntity<Patient> getFormVisitAppointment(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientRepository.save(patient));
    }

}
