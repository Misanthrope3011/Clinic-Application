package com.example.demo1.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

@Getter
@Setter
@AllArgsConstructor
public class PatientDTO {
    Long id;
    private String name;
    private String last_name;
    private String PESEL;
    private String city;
    private String street;
    private String home_number;
    private String postal_code;
    private Long user_id;
}
