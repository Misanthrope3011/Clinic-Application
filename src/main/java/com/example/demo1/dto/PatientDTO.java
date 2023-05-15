package com.example.demo1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatientDTO {
    Long id;
    private String name;
    private String lastName;
    private String PESEL;
    private String city;
    private String street;
    private String homeNumber;
    private String postalCode;
    private Long userId;
}
