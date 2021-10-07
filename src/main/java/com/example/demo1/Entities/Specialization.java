
package com.example.demo1.Entities;

import javax.persistence.*;

@Entity
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String specialization_name;

    @ManyToOne
    @JoinColumn(name = "XD", referencedColumnName = "id", insertable = false, updatable = false)
    Doctor doctor_id;



}
