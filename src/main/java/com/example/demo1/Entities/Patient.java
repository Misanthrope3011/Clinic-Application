package com.example.demo1.Entities;

import javax.persistence.*;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "XD",insertable = false, updatable = false)
    private User user;

    @OneToMany(mappedBy = "id")
    List<DoctorRatings> ratings_by_patient;



}
