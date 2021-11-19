package com.example.demo1.Entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class WorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false)
    Doctor doctorsSchedule;

    private Date day;
    private String start_hour;
    private String end_hour;



}
