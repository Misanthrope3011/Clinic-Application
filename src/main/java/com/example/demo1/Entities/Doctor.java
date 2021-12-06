package com.example.demo1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;


@Getter
@Setter
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String last_name;

    @JsonIgnore
    @JoinColumn(name = "deparment_id")
    @ManyToOne
    Department doctor_department;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    Specialization doctor_specialization;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor_id", cascade = CascadeType.ALL)
    List<MedicalVisit> patient_visits;

    @OneToMany(mappedBy = "doctor")
    List <DoctorRatings> doctor_ratings;


    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    List<WorkSchedule> work_schedule;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "my_id", referencedColumnName = "id")
    private User user;


}


