package com.example.demo1.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class VisitDTO {
    private Long id;
    private Long id_procedure;
    private Long patient_id;
    private Long doctor_id;
    private String visit_start;
    private Date day;
    private String description;
    private String isPaid;
}
