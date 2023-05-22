package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String specializationName;

    @JsonIgnore
    @OneToMany(mappedBy = "doctorSpecialization")
    private List<Doctor> doctorId;


    @OneToMany(mappedBy = "specialization")
    private List<MedicalProcedures> procedures;


}
