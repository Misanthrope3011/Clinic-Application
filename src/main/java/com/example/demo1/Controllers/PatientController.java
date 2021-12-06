package com.example.demo1.Controllers;

import com.example.demo1.DTOs.Prototype;
import com.example.demo1.DTOs.UserDto;
import com.example.demo1.DTOs.VisitDTO;
import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Entities.Patient;
import com.example.demo1.Entities.User;
import com.example.demo1.Enums.UserRole;
import com.example.demo1.MessageResponse;
import com.example.demo1.Prototypes.LoginResponse;
import com.example.demo1.Prototypes.ResponseMessages;
import com.example.demo1.Repositories.*;
import com.example.demo1.Services.VisitFilter;
import com.sun.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private VisitRepository medicalVisitRepository;
    @Autowired
    private MedicalProcedure medicalProcedure;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private VisitFilter visitFilter;

    @GetMapping("/welcome")
    ResponseEntity<ResponseMessages> checkAuthorities() {

        return ResponseEntity.ok(new ResponseMessages("Siema"));
    }

    @GetMapping("/pendingVisits/{id}")
    ResponseEntity getMedicalVisits(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        List <MedicalVisit> pending = new ArrayList<>();
        List <MedicalVisit> history = new ArrayList<>();
        boolean hasAnyPending = false;
        if (patient != null) {

                 List<MedicalVisit> sortedByDate =  patient.getVisits()
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

                if(!hasAnyPending) {
                    history = sortedByDate;
                }

             TreeMap<String, List<MedicalVisit>> visitData = new TreeMap<>();
             visitData.put("Oczekujace", pending);
             visitData.put("Historia", history);

             return ResponseEntity.ok(visitData);
        }
        return ResponseEntity.badRequest().body("Nie znaleziono usera");
    }


    @GetMapping("/sortByDebts/{id}")
    ResponseEntity getDebts(@PathVariable Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient.getVisits() == null) {
            return ResponseEntity.badRequest().body("Nie znaleziono pacjenta");
        }


            List<MedicalVisit> paidVisits = patient.getVisits()
                    .stream().filter(MedicalVisit::isPaid)
                    .collect(Collectors.toList());
            List<MedicalVisit> oweVisits = patient.getVisits()
                    .stream().filter(e -> !(e.isPaid()))
                    .collect(Collectors.toList());

            TreeMap<String, List<MedicalVisit>> isPaid = new TreeMap<>();
            isPaid.put("Zaplacone", paidVisits);
            isPaid.put("NieZaplacone",oweVisits);

            return ResponseEntity.ok(isPaid);
    }

    @GetMapping("/calculateBalance/{id}")
    ResponseEntity calculateBalance(@PathVariable Long id) {
        double payedVisits = 0;
        double totalMoney = 0;

        Patient patient = patientRepository.findById(id).orElse(null);
        List<MedicalVisit> medicalVisits;
        if(patient.getVisits() == null) {
            return ResponseEntity.badRequest().body("XD?");
        }
/*
        if (patippayedVisits = medicalVisits
                                .stream().filter(e -> e.isPaid().equals("Oplacone"))
                                .map(e -> e.getMedicalProcedure().getPrice())
                                .mapToDouble(Double::doubleValue);ent.getVisits() != null) {
            medicalVisits = patient.getVisits();
                    DoubleStream


                    totalMoney = medicalVisits
                    .stream().map(e -> e.getMedicalProcedure().getPrice())
                    .mapToDouble(Double::doubleValue)
                    .sum();

         TreeMap<String, Double> calulcations = new TreeMap<>();
            calulcations.put("Oplacone", payedVisits);
            calulcations.put("Totalnie", totalMoney);

            return ResponseEntity.ok(patient);
        }

 */
        List<Double> prices = patient.getVisits().stream().map(e -> e.getMedicalProcedure().getPrice()).collect(Collectors.toList());

        for (Double price: prices) {
            totalMoney += price;
        }

        List<Double> filtered = patient.getVisits()
                .stream()
                .filter(MedicalVisit::isPaid)
                .map(e -> e.getMedicalProcedure().getPrice()).collect(Collectors.toList());

        for (Double price: filtered) {
            payedVisits += price;
        }
        TreeMap<String, Double> result = new TreeMap();
        result.put("Oplacone", payedVisits);
        result.put("Calosc", totalMoney);

        return ResponseEntity.ok(result);
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
    ResponseEntity<LoginResponse> editInfo(@RequestBody UserDto user) {
        User edited = sampleRepository.findById(user.getId()).orElse(null);

        if(user.getFirstName() != null)
           edited.getPatient().setName(user.getFirstName());
        edited.getPatient().setHome_number(user.getHomeNumber());
        edited.getPatient().setPESEL(user.getPESEL());
        edited.getPatient().setCity(user.getCity());
        edited.getPatient().setStreet(user.getStreet());
        edited.getPatient().setLast_name(user.getLastName());
        sampleRepository.save(edited);

       return ResponseEntity.ok(new LoginResponse(edited.getId(), edited.getEmail(), edited.getPatient(), List.of(UserRole.ROLE_PATIENT.name())));
    }



    @PostMapping("/registerVisit")
    ResponseEntity getFormVisitAppointment(@RequestBody VisitDTO visit) {

        Doctor doctor = doctorRepository.findById(visit.getDoctor_id()).orElse(null);

        List<MedicalVisit> visits = doctor.getPatient_visits();
        List<LocalDateTime> dates = visits.stream().map(MedicalVisit::getStartDate)
                                        .sorted().collect(Collectors.toList());

        String hour = visit.getVisit_start().split(":")[0];
        String minute = visit.getVisit_start().split(":")[1];

        LocalDateTime dateOfVisit = LocalDateTime.of(visit.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute)));

        for (LocalDateTime busyDates: dates) {
            if(LocalDateTime.of(visit.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute))).compareTo(busyDates) == 0) {
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
        patientVisit.setStartDate(LocalDateTime.of(visit.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.of(Integer.parseInt(hourMinute[0]), Integer.parseInt(hourMinute[1]))));

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
        LocalTime localTime = LocalTime.of(9, 0);

        while (localTime.getHour() < 17) {
            listOfHours.add(localTime);
            localTime = localTime.plusMinutes(30);
        }

        Doctor doctor = doctorRepository.findById(visitDTO.getDoctor_id()).orElse(null);
        if(doctor != null) {
            hoursThatDay = doctor.getPatient_visits().stream()
                    .filter(e -> e.getStartDate().getDayOfMonth() == visitDTO.getDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth())
                    .sorted(Comparator.comparing(MedicalVisit::getStartDate))
                    .map(e -> e.getStartDate().toLocalTime())
                    .collect(Collectors.toList());
        }

        for (LocalTime time : hoursThatDay) {
            for (Iterator<LocalTime> datesIterator = listOfHours.iterator(); datesIterator.hasNext(); ) {
                LocalTime temp = datesIterator.next();
                if (temp.compareTo(time) == 0) {
                    datesIterator.remove();
                }
            }
        }

        return ResponseEntity.ok(listOfHours);
    }

}
