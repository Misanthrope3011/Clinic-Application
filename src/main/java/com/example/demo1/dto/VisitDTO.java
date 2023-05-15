package com.example.demo1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class VisitDTO {

    private Long id;
    private Long idProcedure;
    private Long patient;
    private Long doctor;
    private String visit_start;
    private Date day;
    private String description;
    private Boolean isPaid;
    private Boolean hasTookPlace;

}
