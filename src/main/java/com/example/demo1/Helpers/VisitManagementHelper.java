package com.example.demo1.Helpers;

import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Entities.Patient;
import com.example.demo1.Repositories.PatientRepository;
import com.example.demo1.Services.DoctorUtilsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class VisitManagementHelper {

    private final PatientRepository patientRepository;

    public TreeMap<String, List<MedicalVisit>> getVisits(Patient patient) {
        List<MedicalVisit> sortedByDate = patient.getVisits()
                .stream()
                .sorted(Comparator.comparing(MedicalVisit::getStartDate))
                .collect(toList());

        return DoctorUtilsService.prepareMapWithPendingAndHistoryLabels(sortedByDate);
    }

}
