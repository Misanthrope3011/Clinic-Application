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
public class UserService implements UserInterface {

    @Autowired
    SampleRepository sampleRepository;
    TokenRepository tokenRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    VerificationToken verificationToken;

    @Autowired
    EmailSender emailSender;


    @Override
    public User registerNewUsserAccount(com.example.demo1.Entities.User user) {

        boolean isEmailExists = sampleRepository.findByUsername(user.getUsername()).isPresent();

      if(!isEmailExists) {
          user.setEncoded_password(passwordEncoder.encode(user.getEncoded_password()));
          sampleRepository.save(user);
          verificationToken = new VerificationToken(UUID.randomUUID().toString(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(20), user);
          emailSender.sendEmail(user.getUsername(), verificationToken.getToken());
          tokenRepository.save(verificationToken);
      }

        return user;
    }
}
