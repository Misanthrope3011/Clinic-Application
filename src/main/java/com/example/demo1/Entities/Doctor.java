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
    String lastName;

    @JsonIgnore
    @JoinColumn(name = "deparment_id")
    @ManyToOne
    Department doctorDepartment;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    Specialization doctorSpecialization;

    @JsonIgnore
    @OneToMany(mappedBy = "doctorId", cascade = CascadeType.ALL)
    List<MedicalVisit> getPatientVisits;

    @OneToMany(mappedBy = "doctor")
    List <DoctorRatings> doctorRatings;


    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    List<WorkSchedule> workSchedule;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "my_id", referencedColumnName = "id")
    private User user;

    @Lob
    private byte[] image;


}


