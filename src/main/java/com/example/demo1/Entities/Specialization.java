
package com.example.demo1.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String specialization_name;

    @OneToMany(mappedBy = "doctor_specialization")
    List<Doctor> doctor_id;



}
