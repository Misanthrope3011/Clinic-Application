package com.example.demo1.controller;

import com.example.demo1.Entities.*;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Repositories.VisitRepository;
import com.example.demo1.Services.ContactFormService;
import com.example.demo1.Services.DoctorUtilsService;
import com.example.demo1.Services.UserInfoService;
import com.example.demo1.dto.DoctorDTO;
import com.example.demo1.dto.UserDTO;
import com.example.demo1.dto.VisitDTO;
import com.example.demo1.exception.ApplicationException;
import com.example.demo1.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import static com.example.demo1.Services.DoctorUtilsService.splitVisitsOnPendingAndHistory;

@RestController
@AllArgsConstructor
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    private UserInfoService userInfoService;
    private DoctorRepository doctorRepository;
    private ContactFormService contactFormService;
    private PatientRepository patientRepository;
    private VisitRepository visitRepository;
    private DoctorUtilsService doctorUtilsService;

    @DeleteMapping("deletePatient/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        patientRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getNumberOfPatients")
    public ResponseEntity<Integer> getSize() {
        return ResponseEntity.ok(patientRepository.findAll().size());
    }

    @PostMapping("/findByPESEL")
    public ResponseEntity<Patient> findByPESEL(@RequestBody String PESEL) {
        Optional<Patient> patientByPesel = patientRepository.findByPESEL(PESEL);

        return patientByPesel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getTodayVisits/{id}")
    public ResponseEntity<Object> getTodayVisits(@PathVariable Long id) {
        Doctor doctor = doctorUtilsService.findDoctorById(id).orElse(null);
        if (doctor != null) {
            return ResponseEntity.ok(DoctorUtilsService.getTodaysVisits(doctor));
        }
        return new ResponseEntity<>("Nie znaleziono danego doktora ani jego wizyt", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/editPatientProfile")
    public ResponseEntity<Patient> editInfo(@RequestBody UserDTO user) {
        Patient edited = patientRepository.findById(user.getId()).orElseThrow(() -> new ApplicationException("Error fetching user info"));
        EntityUtils.updatePatientData(user, edited);

        return ResponseEntity.ok(patientRepository.save(edited));
    }

    @PutMapping("/editVisit/{id}")
    public ResponseEntity<MedicalVisit> editVisitInfo(@PathVariable Long id, @RequestBody VisitDTO visitDTO) {
        MedicalVisit visit = visitRepository.findById(id).orElseThrow(() -> new ApplicationException("Error fetching user "));
        visit.setPaid(visitDTO.getIsPaid() != null ? visitDTO.getIsPaid() : visit.isPaid());
        visit.setDescription(visitDTO.getDescription() != null ? visitDTO.getDescription() : visit.getDescription());
        visit.setHasTookPlace(visitDTO.getHasTookPlace() != null ? visitDTO.getHasTookPlace() : visit.isHasTookPlace());

        return ResponseEntity.ok(visitRepository.save(visit));
    }

    @PutMapping("/editProfile")
    public ResponseEntity<User> editInfo(@RequestBody DoctorDTO user) {
        User edited = userInfoService.findById(user.getId()).orElseThrow(() -> new ApplicationException("Trouble fetching session"));
        if (edited != null) {
            edited.getDoctor().setName(user.getFirstName() != null ? user.getFirstName() : edited.getDoctor().getName());
            edited.getDoctor().setLastName(user.getLastName() != null ? user.getLastName() : edited.getDoctor().getLastName());
        }
        userInfoService.saveUser(edited);
        return ResponseEntity.ok(edited);
    }

    @PutMapping("editPatient/{id}")
    public ResponseEntity<Doctor> editDoctor(@PathVariable Long id) {
        Doctor doctorToDelete = doctorUtilsService.findDoctorById(id).orElseThrow();
        doctorRepository.delete(doctorToDelete);

        return ResponseEntity.ok(doctorToDelete);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<Doctor> updateDoctorInfo(@RequestBody Doctor doctorToEdit, @PathVariable Integer id, @RequestParam String attributeToChange) {
        return ResponseEntity.ok(doctorToEdit);
    }

    @GetMapping("/pendingVisits/{id}")
    public ResponseEntity<Object> getPendingVisits(@PathVariable Long id, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        TreeMap<String, List<MedicalVisit>> visitData = new TreeMap<>();
        if(doctor.isPresent()) {
            visitData = splitVisitsOnPendingAndHistory(doctor.get());
        }
        return ResponseEntity.ok(visitData);
    }

    @GetMapping("/getVisit/{id}")
    public ResponseEntity<Object> getVisitToEdition(@PathVariable Long id) {
        MedicalVisit visit = visitRepository.findById(id).orElse(null);
        if (visit != null) {
            return ResponseEntity.ok(visit);
        }

        return new ResponseEntity<>("Visit not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getPatient/{id}")
    public ResponseEntity<Patient> getPatients(@PathVariable Long id) {
        return ResponseEntity.ok(patientRepository.findById(id).orElse(null));
    }

    @GetMapping("/contactForm")
    public ResponseEntity<List<ContactForm>> getAllContactForms() {
        return contactFormService.getAllForms();
    }

    @GetMapping("/contactForms")
    public ResponseEntity getAllContactForms(@RequestParam("today") String displayTodayString) {
        if (displayTodayString.equals("today")) {
            return contactFormService.getTodayForms();
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/abandonVisit/{id}")
    public ResponseEntity<Object> deleteVisit(@PathVariable Long id) {
        Optional<MedicalVisit> visit = visitRepository.findById(id);
        if (visit.isEmpty()) {
            return new ResponseEntity<>("Visit not found", HttpStatus.NOT_FOUND);
        }
        visitRepository.delete(visit.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAbandoned")
    public ResponseEntity<List<MedicalVisit>> deleteRequests() {
        return ResponseEntity.ok(doctorUtilsService.getAbandonedVisits());
    }

}
