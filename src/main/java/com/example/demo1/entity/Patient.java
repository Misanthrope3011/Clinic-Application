package com.example.demo1.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Size(min = 7, max = 10, message = "Phone numbers has fixed lengths and can have minimum 7 digits an maximum 10 digits")
    String phone;

    @NotBlank
    @Size(min = 5, max = 30, message = "Minimum 5 characters, max 30 charakters")
    private String name;

    @NotBlank
    @Size(min = 5, max = 30, message = "Minimum 5 characters, max 30 charakters")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}", message = "PESEL consists of 11 digits")
    @Column(unique = true)
    private String PESEL;

    @Size(min = 5, max = 30, message = "Minimum 5 characters, max 30 characters")
    private String city;

    @Size(min = 5, max = 30, message = "Minimum 5 characters, max 30 characters")
    private String street;

    @Size(min = 1, max = 5, message = "Minimum 1 character, max 5 characters")
    private String homeNumber;

    @Pattern(regexp = "^[0-9]{2}[-][0-9]{3}$", message = "Postal code has xx-xxx format")
    private String postalCode;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "my_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    private List<DoctorRatings> rates;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicalVisit> visits;

    public Patient() {
        super();
    }

    public Patient(Long id, String name, String lastName, String PESEL, String city, String street, String homeNumber, String postalCode, User user) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.PESEL = PESEL;
        this.city = city;
        this.street = street;
        this.homeNumber = homeNumber;
        this.postalCode = postalCode;
    }

}
