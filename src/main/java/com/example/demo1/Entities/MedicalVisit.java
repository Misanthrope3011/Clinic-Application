

package com.example.demo1.Entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class MedicalVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String visit_date;
    String description;

    @ManyToOne
    @JoinColumn(name = "doctor", referencedColumnName = "id", insertable = false, updatable = false)
    Doctor doctor_id;

    @ManyToOne
    @JoinColumn(name = "patient",referencedColumnName = "id", insertable = false, updatable = false)
    Patient patient_id;

}
