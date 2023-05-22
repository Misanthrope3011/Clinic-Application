package com.example.demo1.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String openingHour;
    String closingHour;
    String city;
    String street;
    String homeNumber;

    @OneToMany(mappedBy = "doctorDepartment")
    List<Doctor> departmentStaff;

}

