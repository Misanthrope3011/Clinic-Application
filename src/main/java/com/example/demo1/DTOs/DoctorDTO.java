package com.example.demo1.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DoctorDTO {

    String name;
    String second_name;
    String last_name;
    Long specialization_id;
    Long doctor_department_id;
    Long user_id;

}
