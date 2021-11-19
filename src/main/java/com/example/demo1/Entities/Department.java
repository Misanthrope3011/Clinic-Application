
package com.example.demo1.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id_column;

    String name;
    String opening_hour;
    String closing_hour;

    @ManyToOne
    @JoinColumn(name ="Sample", referencedColumnName = "id", insertable = false, updatable = false)
    Doctor doctor_departments;


}

