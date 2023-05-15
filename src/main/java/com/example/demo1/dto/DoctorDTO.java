package com.example.demo1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DoctorDTO {

    private Long id;
    private String firstName;
    private String lastName;

}
