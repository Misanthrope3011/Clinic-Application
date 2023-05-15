

package com.example.demo1.Entities;


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
    boolean isPaid;
    boolean deleteRequest;
    boolean hasTookPlace;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    Doctor doctorId;

    @ManyToOne
    @JoinColumn(name = "procedure_id", referencedColumnName = "id")
    MedicalProcedures medicalProcedure;

    @ManyToOne
    @JoinColumn(name = "patient_id",referencedColumnName = "id")
    Patient patientId;






}

