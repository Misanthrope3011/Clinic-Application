package com.example.demo1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class WorkSchedule {

    private String startHour;
    private String endHour;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "doctor_id")
    Doctor doctor;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
