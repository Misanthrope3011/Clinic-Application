package com.example.demo1.Entities;


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

    public Patient() {
        super();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(min = 5, max = 30, message = "Minimum 5 characters, max 30 charakters")
    private String name;
    @NotBlank
    @Size(min = 5, max = 30, message = "Minimum 5 characters, max 30 charakters")
    private String last_name;
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}", message = "PESEL consists of 11 digits")
    @Column(unique = true)
    private String PESEL;
    @Size(min = 5, max = 30, message = "Minimum 5 characters, max 30 characters")
    private String city;
    @Size(min = 5, max = 30, message = "Minimum 5 characters, max 30 characters")
    private String street;
    @Size(min = 1, max = 5, message = "Minimum 1 character, max 5 characters")
    private String home_number;
    @Pattern(regexp = "^[0-9]{2}[-][0-9]{3}$", message = "Postal code has xx-xxx format")
    private String postal_code;
    @Size(min = 7, max = 10, message = "Phone numbers has fixed lengths and can have minimum 7 digits an maximum 10 digits")
    String phone;

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

    @JsonIgnore
    @OneToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "my_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    private List<DoctorRatings> ratings_by_patient;

    @JsonIgnore
    @OneToMany(mappedBy = "patient_id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicalVisit> visits;

}
