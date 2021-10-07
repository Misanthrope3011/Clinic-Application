

package com.example.demo1.Entities;


import javax.persistence.*;

@Entity
public class MedicalVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String visit_date;
    String description;

    @ManyToOne
    @JoinColumn(name = "XD", referencedColumnName = "id", insertable = false, updatable = false)
    Doctor doctor_id;

    @ManyToOne
    @JoinColumn(name = "XD",referencedColumnName = "id", insertable = false, updatable = false)
    Patient patient_id;

}
