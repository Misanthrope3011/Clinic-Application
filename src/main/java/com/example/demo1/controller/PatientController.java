package com.example.demo1.controller;

import com.example.demo1.Entities.*;
import com.example.demo1.Enums.UserRole;
import com.example.demo1.Helpers.DoctorRatesHelper;
import com.example.demo1.Helpers.VisitManagementHelper;
import com.example.demo1.Prototypes.LoginResponse;
import com.example.demo1.Repositories.*;
import com.example.demo1.Services.DoctorUtilsService;
import com.example.demo1.dto.RateDTO;
import com.example.demo1.dto.UserDTO;
import com.example.demo1.dto.VisitDTO;
import com.example.demo1.exception.ApplicationException;
import com.example.demo1.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private PatientRepository patientRepository;
    private UserRepository sampleRepository;
    private VisitRepository medicalVisitRepository;
    private MedicalProcedure medicalProcedure;
    private DoctorRepository doctorRepository;
    private RatesRepository ratesRepository;
    private VisitManagementHelper managementHelper;
    private DoctorRatesHelper ratesHelper;
    private DoctorUtilsService doctorUtilsService;

    @GetMapping("/pendingVisits/{id}")
    private ResponseEntity getMedicalVisits(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient != null) {
            return ResponseEntity.ok(managementHelper.getVisits(patient));
        }

        return ResponseEntity.badRequest().body("Blad przy pobieraniu danych");
    }

    @GetMapping("/sortByDebts/{id}")
    private ResponseEntity getDebts(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        if (patient.getVisits() == null) {
            return new ResponseEntity("Nie znaleziono pacjenta", HttpStatus.NOT_FOUND);
        }

        List<MedicalVisit> paidVisits = patient.getVisits()
                .stream().filter(MedicalVisit::isPaid)
                .collect(toList());
        List<MedicalVisit> oweVisits = patient.getVisits()
                .stream().filter(e -> !(e.isPaid()))
                .collect(toList());

        TreeMap<String, List<MedicalVisit>> isPaid = new TreeMap<>();
        isPaid.put("Zaplacone", paidVisits);
        isPaid.put("NieZaplacone", oweVisits);

        return ResponseEntity.ok(isPaid);
    }

    @GetMapping("/calculateBalance/{id}")
    private ResponseEntity calculateBalance(@PathVariable Long id) {
        double paidVisits = 0.0;
        double totalMoney = 0.0;

        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient.getVisits() == null) {
            return new ResponseEntity("Nie znaleziono wizyty", HttpStatus.NOT_FOUND);
        }

        totalMoney = patient.getVisits().stream()
                .map(e -> e.getProcedure().getPrice()).reduce(0.0, (e1, e2) -> e1 += e2);

        paidVisits = patient.getVisits()
                .stream()
                .filter(MedicalVisit::isPaid)
                .map(e -> e.getProcedure().getPrice())
                .reduce(0.0, (visitPricePrev, visitPriceNext) -> visitPricePrev += visitPriceNext);

        TreeMap<String, Double> result = new TreeMap();
        result.put("Oplacone", paidVisits);
        result.put("Calosc", totalMoney);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/getProfile")
    private ResponseEntity getPatient(@RequestBody Long id) {
        User user = sampleRepository.findAll().get(id.intValue());
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return new ResponseEntity("Wystapil blad serwera", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PutMapping("/editProfile")
    private ResponseEntity<LoginResponse> editInfo(@RequestBody UserDTO user) {
        User edited = sampleRepository.findById(user.getId()).orElseThrow(() -> new ApplicationException("Blad podczas przetwarzania żądania"));
        EntityUtils.updateUser(user, edited);
        sampleRepository.save(edited);

        return ResponseEntity.ok(new LoginResponse(edited.getId(), edited.getEmail(), edited.getPatient(), List.of(UserRole.ROLE_PATIENT.name())));
    }


    @GetMapping("/getDoctorRates")
    private ResponseEntity calculateRating() {
        List<Doctor> doctor = doctorRepository.findAll();

        return ResponseEntity.ok(ratesHelper.getDoctorAverageRates(doctor));
    }

    @GetMapping("/getUserRates/{id}")
    private ResponseEntity getRates(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) {
            return new ResponseEntity("Patient does not exists", HttpStatus.NOT_FOUND);
        }
        List<DoctorRatings> sortedByDoctor = ratesHelper.sortRatesByDoctorId(patient);

        return ResponseEntity.ok(ratesHelper.formRatesVector(sortedByDoctor, doctorRepository));

    }

    @PostMapping("/registerVisit")
    private ResponseEntity getFormVisitAppointment(@RequestBody VisitDTO visit) {
        Doctor doctor = doctorRepository.findById(visit.getDoctor()).orElse(null);
        List<MedicalVisit> visits = doctor.getGetPatientVisits();
        List<LocalDateTime> dates = visits.stream().map(MedicalVisit::getStartDate)
                .sorted().collect(toList());

        String hour = visit.getVisit_start().split(":")[0];
        String minute = visit.getVisit_start().split(":")[1];
        for (LocalDateTime busyDates : dates) {
            if (LocalDateTime.of(visit.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.of(Integer.parseInt(hour),
                    Integer.parseInt(minute))).compareTo(busyDates) == 0) {
                return new ResponseEntity("Zajety termin", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        MedicalVisit patientVisit = new MedicalVisit();
        patientVisit.setDescription(visit.getDescription());
        patientVisit.setDoctor(doctorRepository.findById(visit.getDoctor()).orElse(null));
        patientVisit.setPatient(patientRepository.findById(visit.getPatient()).orElse(null));
        patientVisit.setProcedure(medicalProcedure.findById(visit.getIdProcedure()).orElse(null));
        patientVisit.setPaid(false);
        patientVisit.setDeleteRequest(false);
        String[] hourMinute = visit.getVisit_start().split(":");
        patientVisit.setStartDate(LocalDateTime.of(visit.getDay().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalTime.of(Integer.parseInt(hourMinute[0]), Integer.parseInt(hourMinute[1]))));

        return ResponseEntity.ok(medicalVisitRepository.save(patientVisit));
    }


    @GetMapping("/deleteRequest/{id}")
    private ResponseEntity<Object> setDeleteRequest(@PathVariable Long id) {
        MedicalVisit visit = medicalVisitRepository.findById(id).orElse(null);
        if (visit == null) {
            return ResponseEntity.badRequest().body("Nie znaleziono wizyty");
        } else {
            visit.setDeleteRequest(true);
            medicalVisitRepository.save(visit);
            return ResponseEntity.ok(visit);
        }
    }

    @PostMapping("/getDoctorHours")
    private ResponseEntity<List<LocalTime>> getAvailableHours(@RequestBody VisitDTO visitDTO) {
        return ResponseEntity.ok(doctorUtilsService.getDoctorAvailableHours(visitDTO));
    }

    @PutMapping("updateRate/{id}")
    private ResponseEntity editInfo(@PathVariable Long id, @RequestBody RateDTO rate) {

        DoctorRatings rateToUpdate = ratesRepository.findById(id).orElse(null);

        if (rateToUpdate == null) {
            DoctorRatings ratings = new DoctorRatings();
            ratings.setRating(rate.getRate());
            ratings.setPatient(patientRepository.findById(rate.getPatientId()).orElse(null));
            ratings.setDoctor(doctorRepository.findById(rate.getDoctorId()).orElse(null));
            return ResponseEntity.ok(ratesRepository.save(ratings));
        } else {
            rateToUpdate.setRating(rate.getRate());
            return ResponseEntity.ok(ratesRepository.save(rateToUpdate));
        }
    }

}
