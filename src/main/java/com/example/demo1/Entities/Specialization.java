
package com.example.demo1.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String specialization_name;

    @ManyToOne
    @JoinColumn(name = "XD", referencedColumnName = "id", insertable = false, updatable = false)
    Doctor doctor_id;



}
