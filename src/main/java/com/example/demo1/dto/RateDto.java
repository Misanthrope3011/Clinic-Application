package com.example.demo1.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RateDto {
    private Long rateId;
    private Double rate;
    private Long patient_id;
    private Long doctor_id;

}

