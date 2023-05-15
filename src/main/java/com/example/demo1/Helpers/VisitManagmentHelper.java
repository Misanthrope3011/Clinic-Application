package com.example.demo1.Helpers;
import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Entities.Patient;
import com.example.demo1.Repositories.PatientRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

@Service
@Getter
@Setter
public class VisitManagmentHelper {

    @Autowired
    private PatientRepository patientRepository;

    public TreeMap getVisits(Patient patient) {
        TreeMap<String, List<MedicalVisit>> visitData = new TreeMap<>();
        List<MedicalVisit> pending = new ArrayList<>();
        List<MedicalVisit> history = new ArrayList<>();
        boolean hasAnyPending = false;
        if (patient != null) {

            List<MedicalVisit> sortedByDate = patient.getVisits()
                    .stream()
                    .sorted(Comparator.comparing(MedicalVisit::getStartDate))
                    .collect(toList());

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

            visitData.put("Oczekujace", pending);
            visitData.put("Historia", history);
        }

        return visitData;
    }





}
