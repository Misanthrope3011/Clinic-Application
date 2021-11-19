package com.example.demo1.Entities;

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
    @OneToMany(mappedBy = "id_column")
    List<Department> department_list;

    @OneToMany(mappedBy = "id")
    List<Specialization> doctor_specializations;

    @OneToMany(mappedBy = "id")
    List<MedicalVisit> patient_visits;

    @OneToMany(mappedBy = "id")
    List <DoctorRatings> doctor_ratings;

    @OneToMany(mappedBy = "id")
    List<WorkSchedule> work_schedule;


}


