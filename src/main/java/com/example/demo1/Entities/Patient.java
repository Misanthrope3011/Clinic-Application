package com.example.demo1.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private String last_name;
    private String PESEL;
    private String city;
    private String street;
    private String home_number;
    private String postal_code;



    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratings", referencedColumnName = "id")
    private List<DoctorRatings> ratings_by_patient;



}
