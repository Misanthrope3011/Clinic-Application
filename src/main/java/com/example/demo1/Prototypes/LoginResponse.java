package com.example.demo1.Prototypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {

    String token;
    Long id;
    String username;
    String email;
    List<String> roles;


}
