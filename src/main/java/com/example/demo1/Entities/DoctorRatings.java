
package com.example.demo1.Entities;


import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.Patient;

import javax.persistence.*;

@Entity
public class DoctorRatings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double rating;
    String rating_explanation;

    @ManyToOne
    @JoinColumn(name = "XD", referencedColumnName = "id", insertable = false, updatable = false)
    Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "XD", referencedColumnName = "id",insertable = false, updatable = false)
    Patient patient;

}
