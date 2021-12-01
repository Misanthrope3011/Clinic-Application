

package com.example.demo1.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class MedicalVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime startDate;
    LocalDateTime endDate;
    String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    Doctor doctor_id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_id",referencedColumnName = "id")
    Patient patient_id;

}
