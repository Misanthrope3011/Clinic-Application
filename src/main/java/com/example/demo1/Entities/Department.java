
package com.example.demo1.Entities;

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
    Long id_column;

    String name;
    String opening_hour;
    String closing_hour;
    String city;
    String street;
    String homeNumber;

    @OneToMany(mappedBy = "doctor_department")
    List<Doctor> department_stuff;



}

