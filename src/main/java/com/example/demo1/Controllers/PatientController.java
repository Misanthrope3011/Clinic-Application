package com.example.demo1.Controllers;

import com.example.demo1.DTOs.Prototype;
import com.example.demo1.DTOs.VisitDTO;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Entities.Patient;
import com.example.demo1.Entities.User;
import com.example.demo1.Helpers.VisitManagmentHelper;
import com.example.demo1.Prototypes.ResponseMessages;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.Repositories.MedicalVisitRepository;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Repositories.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/patient")
@CrossOrigin("http://localhost:4200")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private MedicalVisitRepository medicalVisitRepository;
    @Autowired
    private DoctorRepository doctorRepository;


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
    @PostMapping("/registerVisit")
    ResponseEntity<MedicalVisit> getFormVisitAppointment(@RequestBody VisitDTO visit) {

        MedicalVisit patientVisit = new MedicalVisit();
        patientVisit.setDescription(visit.getDescription());
        patientVisit.setDoctor_id(doctorRepository.findById(visit.getDoctor_id()).orElse(null));
        patientVisit.setPatient_id(patientRepository.findById(visit.getPatient_id()).orElse(null));
        patientVisit.setStartDate(VisitManagmentHelper.createDateFromString(visit.getVisit_start()));
        patientVisit.setEndDate(VisitManagmentHelper.createDateFromString(visit.getVisit_end()));



        return ResponseEntity.ok(patientVisit);
    }

}
