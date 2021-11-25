
package com.example.demo1.Entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class DoctorRatings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Double rating;
    String rating_explanation;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", insertable = false, updatable = false)
    Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id",insertable = false, updatable = false)
    Patient patient;

}
