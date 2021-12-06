package com.example.demo1.Controllers;

import com.example.demo1.DTOs.DoctorDTO;
import com.example.demo1.DTOs.UserDto;
import com.example.demo1.DTOs.VisitDTO;
import com.example.demo1.Entities.*;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Repositories.SampleRepository;
import com.example.demo1.Repositories.VisitRepository;
import com.example.demo1.Services.ContactFormService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private VisitRepository visitRepository;



    @DeleteMapping("deletePatient/{id}")
    ResponseEntity deleteDoctor(@PathVariable Long id) {
        Patient patient = Objects.requireNonNull(patientRepository.findById(id).orElse(null));

        patientRepository.deleteById(Objects.requireNonNull(patientRepository.findById(patient.getId())
                .map(Patient::getId).orElse(null)));
       // sampleRepository.deleteById(id);
        return ResponseEntity.ok("Usunieto");
    }

    @PutMapping("/editPatientProfile")
    ResponseEntity editInfo(@RequestBody UserDto user) {
        Patient edited = patientRepository.findById(user.getId()).orElse(null);

        if(user.getFirstName() != null) {
            edited.setName(user.getFirstName());
            edited.setHome_number(user.getHomeNumber());
            edited.setPESEL(user.getPESEL());
            edited.setCity(user.getCity());
            edited.setStreet(user.getStreet());
            edited.setLast_name(user.getLastName());
            patientRepository.save(edited);
        }

        return ResponseEntity.ok(edited);
    }

    @PutMapping("/editVisit/{id}")
    ResponseEntity editVisitInfo(@PathVariable Long id, @RequestBody VisitDTO visitDTO) {

        MedicalVisit visit = visitRepository.findById(id).orElse(null);

        if(visit != null) {
            visit.setPaid(visitDTO.getIsPaid());
            visit.setDescription(visitDTO.getDescription());

            return ResponseEntity.ok(visitRepository.save(visit));
        }

        return ResponseEntity.badRequest().body("Nie istnieje ta wizyta");
    }
    @PutMapping("/editProfile")
    ResponseEntity editInfo(@RequestBody DoctorDTO user) {
        User edited = sampleRepository.findById(user.getId()).orElse(null);
      if (user.getFirstName() != null)
                edited.getDoctor().setName(user.getFirstName());
        if(user.getLastName() != null)
                edited.getDoctor().setLast_name(user.getLastName());

        sampleRepository.save(edited);

       return ResponseEntity.ok(edited);

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

    @GetMapping("/pendingVisits/{id}")
    ResponseEntity getPendingVisits(@PathVariable Long id) {

        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if(doctor != null) {
            return ResponseEntity.ok(doctor.getPatient_visits());
        }

        return ResponseEntity.badRequest().body("Nie znaleziono doktora");

    }


    @GetMapping("/getVisit/{id}")
    ResponseEntity getVisitToEdition(@PathVariable Long id) {

        MedicalVisit visit = visitRepository.findById(id).orElse(null);

        if(visit != null) {
            return ResponseEntity.ok(visit);
        }

        return ResponseEntity.badRequest().body("Nie znaleziono wizyty");

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

    @DeleteMapping("/abandonVisit/{id}")
    ResponseEntity deleteVisit(@PathVariable Long id) {
       MedicalVisit visit =  visitRepository.findById(id).orElse(null);

        if(visit == null) {
            ResponseEntity.badRequest().body("Wizyta nie istnieje");
        } else {
            visitRepository.delete(visit);

            return ResponseEntity.ok("Usunieto");
        }
            return ResponseEntity.badRequest().body("Nie znaleziono");
    }

    @GetMapping("/getDeleteRequests")
    ResponseEntity deleteRequests() {

        return ResponseEntity.ok(visitRepository.findAll().stream()
        .filter(e -> e.isDeleteRequest()).collect(Collectors.toList()));
    }

}
