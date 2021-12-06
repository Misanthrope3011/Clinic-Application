package com.example.demo1.Services;

import com.example.demo1.Entities.MedicalVisit;
import com.example.demo1.Repositories.VisitRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Service
public class VisitFilter {

    @Autowired
    private VisitRepository visitRepository;






}
