package com.example.demo1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Patient {



    public Patient() {
        super();
    }

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

    public Patient(Long id, String name, String last_name, String PESEL, String city, String street, String home_number, String postal_code, User user) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.PESEL = PESEL;
        this.city = city;
        this.street = street;
        this.home_number = home_number;
        this.postal_code = postal_code;
    }

    @OneToOne
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratings", referencedColumnName = "id")
    private List<DoctorRatings> ratings_by_patient;



}
