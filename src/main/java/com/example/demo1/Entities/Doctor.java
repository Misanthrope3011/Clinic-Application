package com.example.demo1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String second_name;
    String last_name;

    @JsonIgnore
    @JoinColumn(name = "deparment_id")
    @ManyToOne
    Department doctor_department;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    Specialization doctor_specialization;

    @OneToMany(mappedBy = "doctor_id")
    List<MedicalVisit> patient_visits;

    @OneToMany(mappedBy = "doctor")
    List <DoctorRatings> doctor_ratings;

    @OneToMany(mappedBy = "doctor")
    List<WorkSchedule> work_schedule;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "my_id", referencedColumnName = "id")
    private User user;

}


