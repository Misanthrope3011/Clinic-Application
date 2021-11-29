package com.example.demo1.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    Doctor doctor;

    private Date day;
    private String start_hour;
    private String end_hour;

}
