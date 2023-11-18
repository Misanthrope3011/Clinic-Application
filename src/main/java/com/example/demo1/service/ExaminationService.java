package com.example.demo1.service;


import com.example.demo1.entity.MedicalProcedures;
import com.example.demo1.repository.MedicalProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ExaminationService {

    private MedicalProcedureRepository medicalProceduresRepository;
    private HashMap<String, Double> examinations;

    public ExaminationService() {
        examinations = new HashMap<>();
        examinations.put("Gastroskopia", 280.0);
        examinations.put("By-pass zoladkowy", 1400.0);
        examinations.put("Sesja psychologiczna", 110.0);
        examinations.put("Porada bariatryczna", 130.0);
    }

    public ResponseEntity<HashMap<String, Double>> getExaminations() {
        return ResponseEntity.ok(examinations);
    }

    public ResponseEntity<List<MedicalProcedures>> getProcedures() {
        return ResponseEntity.ok(medicalProceduresRepository.findAll());
    }

    @Autowired
    public void setMedicalProceduresRepository(MedicalProcedureRepository medicalProceduresRepository) {
        this.medicalProceduresRepository = medicalProceduresRepository;
    }

}
