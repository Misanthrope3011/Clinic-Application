package com.example.demo1.Controllers;

import com.example.demo1.DTOs.DoctorDTO;
import com.example.demo1.DTOs.UserDto;
import com.example.demo1.DTOs.VisitDTO;
import com.example.demo1.Entities.*;
import com.example.demo1.MessageResponse;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Repositories.SampleRepository;
import com.example.demo1.Repositories.VisitRepository;
import com.example.demo1.Services.ContactFormService;
import com.sun.mail.iap.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
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

    @GetMapping("/getNumberOfPatients")
    ResponseEntity getSize() {
        return ResponseEntity.ok(patientRepository.findAll().size());
    }

    @PostMapping("/findByPESEL")
    ResponseEntity okay(@RequestBody String PESEL) {
        return ResponseEntity.ok(List.of(patientRepository.findByPESEL(PESEL).orElseThrow()));
    }

    @GetMapping("/getTodayVisits/{id}")
    ResponseEntity getTodayVisits(@PathVariable Long id){

        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if(doctor != null) {
            return ResponseEntity.ok(doctor.getPatient_visits().stream()
                    .filter(e -> e.getStartDate().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
                    .collect(Collectors.toList()));
        }



        return ResponseEntity.badRequest().body(new MessageResponse("Nie znaleziono danego doktora ani jego wizyt"));
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
            edited.setPostal_code(user.getPostalCode());
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
    ResponseEntity getPendingVisits(@PathVariable Long id,  @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size)
    {
        page--;
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        List<MedicalVisit> pending = new ArrayList<>();
        List<MedicalVisit> history = new ArrayList<>();
        boolean hasAnyPending = false;
        if (doctor != null) {

            List<MedicalVisit> sortedByDate = doctor.getPatient_visits()
                    .stream()
                    .sorted(Comparator.comparing(MedicalVisit::getStartDate))
                    .collect(Collectors.toList());

            for (int i = 0; i < sortedByDate.size(); i++) {
                if (sortedByDate.get(i).getStartDate().compareTo(LocalDateTime.now()) > 0) {
                    history = sortedByDate.subList(0, i);
                    pending = sortedByDate.subList(i, sortedByDate.size());
                    hasAnyPending = true;
                    break;
                }
            }

            if (!hasAnyPending) {
                history = sortedByDate;
            }

            TreeMap<String, List<MedicalVisit>> visitData = new TreeMap<>();
                visitData.put("Oczekujace", pending);
                visitData.put("Historia", history);



            return ResponseEntity.ok(visitData);
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

    @GetMapping("/getAbandoned")
    ResponseEntity deleteRequests() {

        return ResponseEntity.ok(visitRepository.findAll().stream()
        .filter(e -> e.isDeleteRequest()).collect(Collectors.toList()));
    }

}
