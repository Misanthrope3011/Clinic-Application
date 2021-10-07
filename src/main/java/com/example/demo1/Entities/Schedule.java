package com.example.demo1.Entities;

import javax.persistence.*;


@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String opening_time;
    String closing_time;

}
