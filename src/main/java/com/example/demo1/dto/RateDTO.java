package com.example.demo1.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RateDTO {
    private Long rateId;
    private Double rate;
    private Long patientId;
    private Long doctorId;

}

