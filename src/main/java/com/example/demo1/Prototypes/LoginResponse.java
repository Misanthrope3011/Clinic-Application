package com.example.demo1.Prototypes;

import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.Patient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {

    String token;
    Long id;
    String username;
    String email;
    Patient patient;
    Doctor doctor;
    List<String> roles;


    public LoginResponse(String token, Long id, String username, String email, Patient patient, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.patient = patient;
        this.roles = roles;
    }

    public LoginResponse(String token, Long id, String username, String email, Doctor doctor, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.doctor = doctor;
        this.roles = roles;
    }
}
