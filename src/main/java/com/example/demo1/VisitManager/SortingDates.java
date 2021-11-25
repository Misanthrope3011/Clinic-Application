package com.example.demo1.VisitManager;

import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Repositories.DoctorRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class SortingDates {

    @Autowired
    private DoctorRepository doctorRepository;
    private Doctor doctor;

    public boolean isEmptyDate(int doctorId, LocalDateTime visitDate) {
        ArrayList<MedicalVisit> getAllDoctorVisits = (ArrayList<MedicalVisit>) doctorRepository.findAll().get(doctorId).getPatient_visits();

        ArrayList<LocalDateTime> sortedDoctorSchedule = (ArrayList<LocalDateTime>) getAllDoctorVisits.stream().map(MedicalVisit::getStartDate).sorted().collect(Collectors.toList());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 0; i < sortedDoctorSchedule.size() - 1; i++) {
            LocalDateTime formattedVisitTime = LocalDateTime.parse(visitDate.toString(), formatter);

            if (!(sortedDoctorSchedule.get(i).compareTo(formattedVisitTime) <= 0 && sortedDoctorSchedule.get(i + 1).compareTo(formattedVisitTime) >= 0)) {
                return false;
            }
        }

        return true;
    }
}
