package com.example.demo1.Controllers;

import com.example.demo1.DTOs.*;
import com.example.demo1.Entities.*;
import com.example.demo1.Enums.UserRole;
import com.example.demo1.Helpers.DoctorRatesHelper;
import com.example.demo1.Helpers.VisitManagmentHelper;
import com.example.demo1.Prototypes.LoginResponse;
import com.example.demo1.Repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {


    private PatientRepository patientRepository;
    private SampleRepository sampleRepository;
    private VisitRepository medicalVisitRepository;
    private MedicalProcedure medicalProcedure;
    private DoctorRepository doctorRepository;
    private RatesRepository ratesRepository;
    private VisitManagmentHelper managementHelper;
    private DoctorRatesHelper ratesHelper;

    @GetMapping("/pendingVisits/{id}")
    ResponseEntity getMedicalVisits(@PathVariable Long id) {
        Patient patient  = patientRepository.findById(id).orElse(null);
        if(patient != null)
            return ResponseEntity.ok(managementHelper.getVisits(patient));
        return ResponseEntity.badRequest().body("Blad przy pobieraniu danych");
    }


    @GetMapping("/sortByDebts/{id}")
    ResponseEntity getDebts(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient.getVisits() == null) {
            return ResponseEntity.badRequest().body("Nie znaleziono pacjenta");
        }

            List<MedicalVisit> paidVisits = patient.getVisits()
                    .stream().filter(MedicalVisit::isPaid)
                    .collect(toList());
            List<MedicalVisit> oweVisits = patient.getVisits()
                    .stream().filter(e -> !(e.isPaid()))
                    .collect(toList());

            TreeMap<String, List<MedicalVisit>> isPaid = new TreeMap<>();
            isPaid.put("Zaplacone", paidVisits);
            isPaid.put("NieZaplacone",oweVisits);

            return ResponseEntity.ok(isPaid);
    }

    @GetMapping("/calculateBalance/{id}")
    ResponseEntity calculateBalance(@PathVariable Long id) {
        double payedVisits = 0.0;
        double totalMoney = 0.0;

        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient.getVisits() == null) {
            return ResponseEntity.badRequest().body("Wystapil blad przy pobieraniu danych pacjenta");
        }

        totalMoney = patient.getVisits().stream()
                .map(e -> e.getMedicalProcedure().getPrice()).reduce( 0.0, (e1, e2) -> e1 += e2 );

        payedVisits = patient.getVisits()
                .stream()
                .filter(MedicalVisit::isPaid)
                .map(e -> e.getMedicalProcedure().getPrice())
                .reduce(0.0, (e1, e2) -> e1 += e2);

        TreeMap<String, Double> result = new TreeMap();
        result.put("Oplacone", payedVisits);
        result.put("Calosc", totalMoney);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/getProfile")
    ResponseEntity getPatient(@RequestBody Long id) {
        User user = sampleRepository.findAll().get(id.intValue());
        if(user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.badRequest().body("Nie znaleziono");
    }


    @PutMapping("/editProfile")
    ResponseEntity<LoginResponse> editInfo(@RequestBody UserDto user) {
        User edited = sampleRepository.findById(user.getId()).orElse(null);

        edited.getPatient().setName(user.getFirstName() != null ? user.getFirstName() : edited.getPatient().getName());
        edited.getPatient().setHome_number(user.getHomeNumber() != null ? user.getHomeNumber() : edited.getPatient().getHome_number());
        edited.getPatient().setPESEL(user.getPESEL() != null ? user.getPESEL() : edited.getPatient().getPESEL());
        edited.getPatient().setCity(user.getCity() != null ? user.getCity() : edited.getPatient().getCity());
        edited.getPatient().setStreet(user.getStreet() != null ? user.getStreet() : edited.getPatient().getStreet());
        edited.getPatient().setLast_name(user.getLastName() != null ? user.getLastName() : edited.getPatient().getLast_name());
        edited.getPatient().setPostal_code(user.getPostalCode() != null ? user.getPostalCode() : edited.getPatient().getLast_name());
        sampleRepository.save(edited);

       return ResponseEntity.ok(new LoginResponse(edited.getId(), edited.getEmail(), edited.getPatient(), List.of(UserRole.ROLE_PATIENT.name())));
    }

    @GetMapping("/getDoctorRates")
    ResponseEntity calculateRating() {
        List<Doctor> doctor = doctorRepository.findAll();


        return ResponseEntity.ok(ratesHelper.getDoctorAverageRates(doctor));
    }

    @GetMapping("/getUserRates/{id}")
    ResponseEntity getRates(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);

        if(patient == null) {
            return new ResponseEntity("Patient does not exists", HttpStatus.NOT_FOUND);
        }

        List<DoctorRatings> sortedByDoctor = ratesHelper.sortRatesByDoctorId(patient);

       return ResponseEntity.ok(ratesHelper.formRatesVector(sortedByDoctor, doctorRepository));

    }

    @PostMapping("/registerVisit")
    ResponseEntity getFormVisitAppointment(@RequestBody VisitDTO visit) {
        Doctor doctor = doctorRepository.findById(visit.getDoctor_id()).orElse(null);
        List<MedicalVisit> visits = doctor.getPatient_visits();
        List<LocalDateTime> dates = visits.stream().map(MedicalVisit::getStartDate)
                                        .sorted().collect(toList());

        String hour = visit.getVisit_start().split(":")[0];
        String minute = visit.getVisit_start().split(":")[1];
        for (LocalDateTime busyDates: dates) {
            if(LocalDateTime.of(visit.getDay().toInstant().
                    atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.of(Integer.parseInt(hour),
                    Integer.parseInt(minute))).compareTo(busyDates) == 0) {
                return ResponseEntity.badRequest().body("Zajety termin");
            }
        }

        MedicalVisit patientVisit = new MedicalVisit();
        patientVisit.setDescription(visit.getDescription());
        patientVisit.setDoctor_id(doctorRepository.findById(visit.getDoctor_id()).orElse(null));
        patientVisit.setPatient_id(patientRepository.findById(visit.getPatient_id()).orElse(null));
        patientVisit.setMedicalProcedure(medicalProcedure.findById(visit.getId_procedure()).orElse(null));
        patientVisit.setPaid(false);
        patientVisit.setDeleteRequest(false);
        String[] hourMinute = visit.getVisit_start().split(":");
        patientVisit.setStartDate(LocalDateTime.of(visit.getDay().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalTime.of(Integer.parseInt(hourMinute[0]), Integer.parseInt(hourMinute[1]))));

        return ResponseEntity.ok(medicalVisitRepository.save(patientVisit));
    }



    @GetMapping("/deleteRequest/{id}")
    ResponseEntity setDeleteRequest(@PathVariable Long id) {

        MedicalVisit visit = medicalVisitRepository.findById(id).orElse(null);
        if(visit == null) {
            return ResponseEntity.badRequest().body("Nie znaleziono wizyty");
        } else {
            visit.setDeleteRequest(true);
            medicalVisitRepository.save(visit);
            return ResponseEntity.ok(visit);
        }
    }

    @PostMapping("/getDoctorHours")
    ResponseEntity getAvailableHours(@RequestBody VisitDTO visitDTO) {
        List<LocalTime> hoursThatDay = new ArrayList<>();
        List<LocalTime> listOfHours = new ArrayList<>();
        LocalTime localTime;

        if(visitDTO.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()
        == LocalDateTime.now().getDayOfMonth() && visitDTO.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        .getMonth().equals(LocalDateTime.now().getMonth())) {
            localTime = LocalTime.of(LocalDateTime.now().getHour() + 1, LocalDateTime.now().getMinute() > 30 ? 0 : 30);
        } else {
            localTime = LocalTime.of(9, 0);
        }
        while (localTime.getHour() < 17) {
            listOfHours.add(localTime);
            localTime = localTime.plusMinutes(30);
        }
        Doctor doctor = doctorRepository.findById(visitDTO.getDoctor_id()).orElse(null);
        if(doctor != null) {
            hoursThatDay = doctor.getPatient_visits().stream()
                    .filter(e -> e.getStartDate().getDayOfMonth() == visitDTO.getDay()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()
                        && e.getStartDate().getMonth().toString().equals(visitDTO.getDay()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().toString())
                    )
                    .sorted(Comparator.comparing(MedicalVisit::getStartDate))
                    .map(e -> e.getStartDate().toLocalTime())
                    .collect(toList());
        }
        for (LocalTime time : hoursThatDay) {
            listOfHours.removeIf(temp -> temp.compareTo(time) == 0);
        }
        return ResponseEntity.ok(listOfHours);
    }

    @PutMapping("updateRate/{id}")
    ResponseEntity editInfo(@PathVariable Long id, @RequestBody RateDto rate) {

        DoctorRatings rateToUpdate = ratesRepository.findById(id).orElse(null);

        if(rateToUpdate == null) {
            DoctorRatings ratings = new DoctorRatings();
            ratings.setRating(rate.getRate());
            ratings.setPatient(patientRepository.findById(rate.getPatient_id()).orElse(null));
            ratings.setDoctor(doctorRepository.findById(rate.getDoctor_id()).orElse(null));
            return ResponseEntity.ok(ratesRepository.save(ratings));
        } else {
            rateToUpdate.setRating(rate.getRate());
            return ResponseEntity.ok(ratesRepository.save(rateToUpdate));
        }
    }

}
