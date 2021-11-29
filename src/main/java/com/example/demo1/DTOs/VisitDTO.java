package com.example.demo1.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VisitDTO {

    private Long patient_id;
    private Long doctor_id;
    private String visit_start;
    private String visit_end;
    private String description;
}
