package com.example.demo1.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class News {

    @Column(length = 1000)
    private String content;
    private String title;
    private String header;
    private LocalDateTime timeOfCreation;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}


