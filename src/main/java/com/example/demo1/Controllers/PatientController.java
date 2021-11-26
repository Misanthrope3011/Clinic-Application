package com.example.demo1.Controllers;

import com.example.demo1.Entities.Patient;
import com.example.demo1.Entities.User;
import com.example.demo1.Prototypes.ResponseMessages;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Repositories.SampleRepository;
import javassist.tools.rmi.Sample;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patient")
@CrossOrigin("http://localhost:4200")
public class PatientController {

    private PatientRepository patientRepository;
    private SampleRepository sampleRepository;

    @GetMapping("/welcome")
    ResponseEntity<ResponseMessages> checkAuthorities() {
        return ResponseEntity.ok(new ResponseMessages("Siema"));
    }

    @GetMapping("/getProfile")
    ResponseEntity getPatient(@RequestBody Long id) {
        User user = sampleRepository.findById(id).orElse(null);
        if(user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.badRequest().body("Nie znaleziono");
    }

    @PostMapping
    ResponseEntity<Patient> getFormVisitAppointment(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientRepository.save(patient));
    }

}
