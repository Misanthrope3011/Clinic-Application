package com.example.demo1.User;

import com.example.demo1.UserInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService implements UserInterface {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUsserAccount() {
        return null;
    }
}
