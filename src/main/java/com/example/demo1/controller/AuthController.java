package com.example.demo1.controller;

import com.example.demo1.entity.User;
import com.example.demo1.entity.VerificationToken;
import com.example.demo1.prototype.Credentials;
import com.example.demo1.service.EmailSender;
import com.example.demo1.service.UserAuthenticationService;
import com.example.demo1.service.UserDetailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@Getter
@Setter
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserAuthenticationService userAuthenticationService;
    private final UserDetailService userDetailService;
    private final EmailSender sender;

    @PostMapping("/signIn")
    public ResponseEntity<Object> signIn(@RequestBody Credentials loginRequest) {
        return userAuthenticationService.loginUser(loginRequest);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody User signUpRequest) throws MessagingException {
        if (userDetailService.existsByUsername(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(("Email is already taken!"));
        }

        if (signUpRequest.getEmail() != null) {
            User requestUser = userAuthenticationService.createUserFromRequest(signUpRequest);
            String token = userAuthenticationService.createUserToken(requestUser);/* STAGE 2
            String REGISTER_MAIL_SUBJECT_TEXT = "Rejestracja konta w przychodni";
            String REGISTER_SUBJECT_BODY = "Dziekujęmy za aktywowanie" +
                    "konta w naszej klinice. Aby aktywowac konto \n" +
                    "<a href = http://localhost:8080/signUp?token=" + token + "> kliknij tutaj </a>";
            sender.sendMail(signUpRequest.getEmail(), REGISTER_MAIL_SUBJECT_TEXT, REGISTER_SUBJECT_BODY);*/
            return ResponseEntity.ok(userDetailService.loadUserByUsername(requestUser.getEmail()));
        }
        return new ResponseEntity<>("Zly email", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/signUp")
    ResponseEntity<Object> newUser(@RequestParam("token") String token) {
        VerificationToken findUserToken = userAuthenticationService.findByToken(token);

        if (findUserToken == null) {
            return new ResponseEntity<>(String.format("Resource with token {%s} not found", token), HttpStatus.NOT_FOUND);
        }

        findUserToken.getUser().setActive(true);
        findUserToken.setConfirmationTime(LocalDateTime.now());

        return new ResponseEntity<>(findUserToken, HttpStatus.OK);
    }

}
