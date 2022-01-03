package com.example.demo1.Services;

import com.example.demo1.EmailVerification.EmailSender;
import com.example.demo1.Entities.User;
import com.example.demo1.Entities.VerificationToken;
import com.example.demo1.Repositories.SampleRepository;
import com.example.demo1.Repositories.TokenRepository;
import com.example.demo1.UserInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Getter
@Setter
public class UserService {

    @Autowired
    SampleRepository sampleRepository;
    TokenRepository tokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    VerificationToken verificationToken;

    @Autowired
    EmailSender emailSender;



}
