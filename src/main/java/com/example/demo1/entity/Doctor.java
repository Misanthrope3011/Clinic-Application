package com.example.demo1.entity;

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
    private Long id;

    private String name;
    private String lastName;

    @JsonIgnore
    @JoinColumn(name = "deparment_id")
    @ManyToOne
    private Department doctorDepartment;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private Specialization doctorSpecialization;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<MedicalVisit> getPatientVisits;

    @OneToMany(mappedBy = "doctor")
    private List<DoctorRatings> doctorRatings;


    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<WorkSchedule> workSchedule;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "my_id", referencedColumnName = "id")
    private User user;

    @Lob
    private byte[] image;


}


