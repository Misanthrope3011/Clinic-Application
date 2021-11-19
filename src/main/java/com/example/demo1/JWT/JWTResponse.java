package com.example.demo1.JWT;

import com.example.demo1.Enums.UserRole;

import java.util.List;

public class JWTResponse {

    private String token;
    private String type = "Basic";
    private String username;
    private String password;
    private List<String> roles;

    public JWTResponse(String jwt, String username, String password, List<String> roles) {
	    this.token = jwt;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
