
package com.example.demo1.Entities;

import javax.persistence.*;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id_column;

    String name;
    String opening_hour;
    String closing_hour;

    @ManyToOne
    @JoinColumn(name ="id", referencedColumnName = "id", insertable = false, updatable = false)
    Doctor doctor_departments;


}

