package com.example.demo1.Services;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ExaminationService {


   HashMap<String, Double> examinations;


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
}
