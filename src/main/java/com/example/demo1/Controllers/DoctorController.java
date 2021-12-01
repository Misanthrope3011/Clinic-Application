package com.example.demo1.Controllers;

import com.example.demo1.DTOs.DoctorDTO;
import com.example.demo1.DTOs.UserDto;
import com.example.demo1.Entities.*;
import com.example.demo1.MessageResponse;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Repositories.SampleRepository;
import com.example.demo1.Services.ContactFormService;
import javassist.tools.rmi.Sample;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    @Autowired
    private SampleRepository sampleRepository;
    private DoctorRepository doctorRepository;
    private ContactFormService contactFormService;
    private PatientRepository patientRepository;

    @DeleteMapping("deletePatient/{id}")
    ResponseEntity deleteDoctor(@PathVariable Long id) {
        Patient patient = Objects.requireNonNull(patientRepository.findById(id).orElse(null));

        patientRepository.deleteById(Objects.requireNonNull(patientRepository.findById(patient.getId())
                .map(Patient::getId).orElse(null)));
       // sampleRepository.deleteById(id);
        return ResponseEntity.ok("Usunieto");
    }



    @PutMapping("/editProfile")
    ResponseEntity<User> editInfo(@RequestBody DoctorDTO user) {
        User edited = sampleRepository.findById(user.getId()).orElse(null);
        assert edited != null;
        if (user.getName() != null)
                edited.getDoctor().setName(user.getName());
        if(user.getLast_name() != null)
                edited.getDoctor().setLast_name(user.getLast_name());
        return ResponseEntity.ok(sampleRepository.save(edited));
    }

    @PutMapping("editPatient/{id}")
    ResponseEntity<Doctor> editDoctor(@PathVariable Integer id) {
        Doctor doctorToDelete = doctorRepository.findById(id.longValue()).orElseThrow();
        doctorRepository.delete(doctorToDelete);
        return ResponseEntity.ok(doctorToDelete);
    }

    @PutMapping("edit/{id}")
    ResponseEntity<Doctor> updateDoctorInfo(@RequestBody Doctor doctorToEdit, @PathVariable Integer id, @RequestParam String attributeToChange) {

        return ResponseEntity.ok(doctorToEdit);
    }

    @GetMapping("/pendingVisits")
    ResponseEntity getPendingVisits(@RequestParam Long id) {

        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if(doctor != null) {
            return ResponseEntity.ok(doctor.getPatient_visits());
        }

        return ResponseEntity.badRequest().body("Nie znaleziono doktora");

    }

    @GetMapping("/getPatient/{id}")
    public ResponseEntity<Patient> getPatients(@PathVariable Long id) {
        return ResponseEntity.ok(patientRepository.findById(id).orElse(null));
    }

    @GetMapping("/contactForm")
    ResponseEntity<List<ContactForm>> getAllContactForms() {
        return contactFormService.getAllForms();
    }

    @GetMapping("/contactForms")
    ResponseEntity getAllContactForms(@RequestParam ("today") String displayTodayString) {
        if(displayTodayString.equals("today")) {
            return contactFormService.getTodayForms();
        }

        return (ResponseEntity) ResponseEntity.badRequest();
    }

}
