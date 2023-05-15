package com.example.demo1.Prototypes;

import com.example.demo1.Entities.Doctor;
import com.example.demo1.Entities.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {

    private byte[] image;
    private String token;
    private Long id;
    private String username;
    private String email;
    private Patient patient;
    private Doctor doctor;
    private List<String> roles;


    public LoginResponse(String token, Long id, String username, String email, Patient patient, List<String> roles, byte[] image) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.patient = patient;
        this.roles = roles;
        this.image = image;

    }

    public LoginResponse(String token, Long id, String username, String email, Doctor doctor, List<String> roles, byte[] image) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.doctor = doctor;
        this.roles = roles;
        this.image = image;

    }

    public LoginResponse(Long id, String email, Patient patient, List<String> roles) {
        this.id = id;
        this.email = email;
        this.patient = patient;
        this.roles = roles;
    }


    public LoginResponse(String token, String email, List<String> roles) {
        this.token = token;
        this.email = email;
        this.roles = roles;
    }
}
