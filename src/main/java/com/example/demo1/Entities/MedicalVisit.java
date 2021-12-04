

package com.example.demo1.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    Doctor doctor_id;

    @ManyToOne
    @JoinColumn(name = "procedure_id", referencedColumnName = "id")
    MedicalProcedures medicalProcedure;

    @ManyToOne
    @JoinColumn(name = "patient_id",referencedColumnName = "id")
    Patient patient_id;

    String isPayed;





}

