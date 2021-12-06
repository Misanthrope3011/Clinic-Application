package com.example.demo1.Services;


import com.example.demo1.Entities.MedicalProcedures;
import com.example.demo1.Repositories.MedicalProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ExaminationService {

    @Autowired
    private MedicalProcedure medicalProceduresRepository;

    HashMap<String, Double> examinations;


    public ExaminationService() {
        List<MedicalProcedures> medicalProcedures = new ArrayList<>();

        examinations = new HashMap<>();
       examinations.put("Gastroskopia", 280.0);
       examinations.put("By-pass zoladkowy", 1400.0);
       examinations.put("Sesja psychologiczna", 110.0);
       examinations.put("Porada bariatryczna", 130.0);
    }

    public ResponseEntity <List<MedicalProcedures>> getProcedures() {
        return ResponseEntity.ok(medicalProceduresRepository.findAll());
    }
    public ResponseEntity<HashMap<String, Double>> getExaminations() {
        return ResponseEntity.ok(examinations);
    }
}
