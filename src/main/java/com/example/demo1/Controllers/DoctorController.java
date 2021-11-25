package com.example.demo1.Controllers;

import com.example.demo1.Entities.ContactForm;
import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.Services.ContactFormService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;
    private ContactFormService contactFormService;

    @DeleteMapping("doctor/delete/{id}")
    ResponseEntity<Doctor> deleteDoctor(@PathVariable Integer id) {
        Doctor doctorToDelete = doctorRepository.findById(id.longValue()).orElseThrow();
        doctorRepository.delete(doctorToDelete);
        return ResponseEntity.ok(doctorToDelete);
    }

    @PutMapping("doctor/edit/{id}")
    ResponseEntity<Doctor> updateDoctorInfo(@RequestBody Doctor doctorToEdit, @PathVariable Integer id, @RequestParam String attributeToChange) {

        return ResponseEntity.ok(doctorToEdit);
    }

    @GetMapping("doctor/pendingVisits")
    ResponseEntity getPendingVisits(@RequestParam Long id) {

        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if(doctor != null) {
            return ResponseEntity.ok((ArrayList<MedicalVisit>) doctor.getPatient_visits());
        }

        return ResponseEntity.badRequest().body("Nie znaleziono doktora");

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
