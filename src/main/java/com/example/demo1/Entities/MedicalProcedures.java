package com.example.demo1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MedicalProcedures {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
    Double price;

    byte[] image;

    @Size(min = 10, message = "Description is too short")
    String description;

    @JsonIgnore
    @OneToMany(mappedBy = "medicalProcedure")
    List<MedicalVisit> visitForProcedure;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    Specialization specialization;

    byte[] procedureDocument;

}
