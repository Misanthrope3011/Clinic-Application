package com.example.demo1.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ContactForm {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String content;
    private LocalDateTime date;
}
