package com.example.demo1.helpers;

import com.example.demo1.entity.MedicalVisit;
import com.example.demo1.entity.Patient;
import com.example.demo1.repository.PatientRepository;
import com.example.demo1.service.DoctorUtilsService;
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
