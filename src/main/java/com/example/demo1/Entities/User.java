package com.example.demo1.Entities;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;
    String encoded_password;
    String sign_up_date;
    boolean is_active;

}
