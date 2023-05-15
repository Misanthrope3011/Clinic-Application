package com.example.demo1.Services;


import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Repositories.DoctorRepository;
import com.example.demo1.dto.VisitDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DoctorUtilsService {

    private final DoctorRepository doctorRepository;

    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<LocalTime> getDoctorAvailableHours(VisitDTO visitDTO) {
        List<LocalTime> hoursGivenDay = new ArrayList<>();
        List<LocalTime> listOfHours = new ArrayList<>();
        int requestedDay = visitDTO.getDay().
                toInstant().atZone(ZoneId.systemDefault()).
                toLocalDate().getDayOfMonth();
        Month requestedMonth =  visitDTO.getDay()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .getMonth();

        LocalTime localTime;

        if(requestedDay == LocalDateTime.now().getDayOfMonth() && requestedMonth.equals(LocalDateTime.now().getMonth())) {
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
            hoursGivenDay = doctor.getGetPatientVisits().stream()
                    .filter(e -> e.getStartDate().getDayOfMonth() == visitDTO.getDay()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth()
                            && e.getStartDate().getMonth().toString().equals(visitDTO.getDay()
                            .toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().toString())
                    )
                    .sorted(Comparator.comparing(MedicalVisit::getStartDate))
                    .map(e -> e.getStartDate().toLocalTime())
                    .collect(toList());
        }
        for (LocalTime time : hoursGivenDay) {
            listOfHours.removeIf(temp -> temp.equals(time));
        }
        return listOfHours;
    }
}
