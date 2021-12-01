package com.example.demo1.Entities;

import javax.persistence.*;

@Entity
public class MedicalProcedures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
    Double price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    Specialization specialization;
}
