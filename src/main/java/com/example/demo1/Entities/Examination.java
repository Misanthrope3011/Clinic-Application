package com.example.demo1.Entities;

import com.example.demo1.Enums.ExaminationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    ExaminationType examinationType;

    String examination_date;

}
